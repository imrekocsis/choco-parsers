/*
 * Copyright (c) 1999-2015, Ecole des Mines de Nantes
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Ecole des Mines de Nantes nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.parser.flatzinc.layout;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.solution.Solution;

import java.util.List;

/**
 * Created by cprudhom on 18/06/15.
 * Project: choco-parsers.
 */
public class SharedSolutionPrinter extends ASolutionPrinter {

    List<Solver> slvrs;
    Solver finisher = null;
    boolean fo = false;
    Integer bestObj = null;


    public SharedSolutionPrinter(List<Solver> slvrs, boolean printAll, boolean printStat) {
        super(printAll, printStat);
        this.slvrs = slvrs;
        for (int i = 0; i < slvrs.size(); i++) {
            final int widx = i;
            slvrs.get(i).plugMonitor((IMonitorSolution) () -> myOnSol(widx));

        }
    }

    private synchronized void myOnSol(int wIdx) {
        boolean print = true;
        if (slvrs.get(0).getObjectiveManager().getPolicy() != ResolutionPolicy.SATISFACTION) {
            int x = slvrs.get(wIdx).getObjectiveManager().getBestSolutionValue().intValue();
            if (bestObj == null) {
                bestObj = x;
            } else {
                if (slvrs.get(0).getObjectiveManager().getPolicy() == ResolutionPolicy.MAXIMIZE) {
                    if (x <= bestObj) {
                        print = false;
                    } else {
                        bestObj = x;
                        slvrs.forEach(solver -> solver.getSearchLoop().getObjectiveManager().updateBestLB(x));
                    }
                } else {
                    if (x >= bestObj) {
                        print = false;
                    } else {
                        bestObj = x;
                        slvrs.forEach(solver -> solver.getSearchLoop().getObjectiveManager().updateBestUB(x));
                    }
                }
            }
        }
        bestSolution.record(slvrs.get(wIdx));
        if (print) {
            onSolution();
        }
    }

    @Override
    public synchronized void onSolution() {
        super.onSolution();
        if (printStat) {
            statistics();
        }
    }

    @Override
    public synchronized void printSolution(Solution solution) {
        synchronized (solution) {
            super.printSolution(solution);
        }
    }

    public synchronized void doFinalOutPut() {
        if (!fo) {
            fo = true;
            userinterruption = false;
            boolean complete = isComplete();
            if (nbSolution > 0) {
                printSolution(bestSolution);
            }
            if (complete) {
                if (nbSolution > 0) {
                    System.out.printf("==========\n");
                } else {
                    System.out.printf("=====UNSATISFIABLE=====\n");
                }
            } else {
                if (slvrs.get(0).getObjectiveManager().isOptimization()) {
                    System.out.printf("=====UNBOUNDED=====\n");
                } else {
                    System.out.printf("=====UNKNOWN=====\n");
                }
            }
            if (printStat) {
                statistics();
            }
        }
    }

    private boolean isComplete() {
        return finisher != null && !finisher.getSearchLoop().hasReachedLimit() && !finisher.getSearchLoop().hasReachedLimit();
    }

    private void statistics() {
        StringBuilder st = new StringBuilder(256);
        st.append(String.format("%d Solutions, ", nbSolution));
        switch (slvrs.get(0).getObjectiveManager().getPolicy()) {
            case MINIMIZE: {
                st.append("Minimize ");
                st.append(slvrs.get(0).getObjectiveManager().getObjective().getName()).append(" = ");
                int best = slvrs.get(0).getObjectiveManager().getBestUB().intValue();
                for (int i = 1; i < slvrs.size(); i++) {
                    if (best > slvrs.get(i).getObjectiveManager().getBestUB().intValue()) {
                        best = slvrs.get(i).getObjectiveManager().getBestUB().intValue();
                    }
                }
                st.append(best).append(", ");
            }
            break;
            case MAXIMIZE: {
                st.append("Maximize ");
                st.append(slvrs.get(0).getObjectiveManager().getObjective().getName()).append(" = ");
                int best = slvrs.get(0).getObjectiveManager().getBestLB().intValue();
                for (int i = 1; i < slvrs.size(); i++) {
                    if (best < slvrs.get(i).getObjectiveManager().getBestLB().intValue()) {
                        best = slvrs.get(i).getObjectiveManager().getBestLB().intValue();
                    }
                }
                st.append(best).append(", ");
            }
            break;

        }
        long[] stats = new long[]{0, 0, 0, 0, 0};
        for (int i = 0; i < slvrs.size(); i++) {
            stats[0] = Math.max(stats[0], (long) (slvrs.get(i).getMeasures().getTimeCount() * 1000));
            stats[1] = Math.max(stats[1], slvrs.get(i).getMeasures().getNodeCount());
            stats[2] = Math.max(stats[2], slvrs.get(i).getMeasures().getBackTrackCount());
            stats[3] = Math.max(stats[3], slvrs.get(i).getMeasures().getFailCount());
            stats[4] = Math.max(stats[4], slvrs.get(i).getMeasures().getRestartCount());
        }
        st.append(String.format("Resolution %.3fs, %d Nodes (%,.1f n/s), %d Backtracks, %d Fails, %d Restarts",
                stats[0] / 1000f, stats[1], stats[1] / (stats[0] / 1000f), stats[2], stats[3], stats[4]));
        System.out.printf("%% %s\n", st.toString());
    }

    @Override
    public synchronized void imdone(Solver solver) {
        if (finisher == null) {
            finisher = solver;
        }
    }
}