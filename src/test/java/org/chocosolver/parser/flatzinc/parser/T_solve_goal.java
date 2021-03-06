/**
 * Copyright (c) 2014, chocoteam
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the {organization} nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.parser.flatzinc.parser;

import org.chocosolver.parser.flatzinc.Flatzinc4Parser;
import org.chocosolver.parser.flatzinc.ast.Datas;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * <br/>
 *
 * @author Charles Prud'homme
 * @since 18/10/12
 */
public class T_solve_goal extends GrammarTest {

    Model mSolver;
    Datas datas;

    @BeforeMethod
    public void before() {
        mSolver = new Model();
        datas = new Datas();
    }

    @Test(groups = "1s")
    public void testSatisfy() throws IOException {
        Flatzinc4Parser fp = parser("solve satisfy;", mSolver, datas);
        fp.solve_goal();
    }

    @Test(groups = "1s")
    public void testMaximize() throws IOException {
        datas.register("a", mSolver.intVar("a", 0, 10, true));
        Flatzinc4Parser fp = parser("solve maximize a;", mSolver, datas);
        fp.solve_goal();
    }

    @Test(groups = "1s")
    public void testMinimize() throws IOException {
        datas.register("a", mSolver.intVar("a", 0, 10, true));
        Flatzinc4Parser fp = parser("solve minimize a;", mSolver, datas);
        fp.solve_goal();
    }

    @Test(groups = "1s")
    public void testSatisfy2() throws IOException {
        datas.register("a", mSolver.intVar("a", 0, 10, true));
        Flatzinc4Parser fp = parser("solve ::int_search([a],input_order,indomain_min, complete) satisfy;", mSolver, datas);
        fp.solve_goal();
    }


    @Test(groups = "1s")
    public void testSatisfy3() throws IOException {
        datas.register("r", mSolver.intVarArray("r", 5, 0, 10, true));
        datas.register("s", mSolver.intVarArray("s", 5, 0, 10, true));
        datas.register("o", mSolver.intVar("o", 0, 10, true));
        Flatzinc4Parser fp = parser(
                "solve\n" +
                        "  ::seq_search(\n" +
                        "    [ int_search(r, input_order, indomain_min, complete),\n" +
                        "      int_search(s, input_order, indomain_min, complete) ])\n" +
                        "  minimize o;", mSolver, datas
        );
        fp.solve_goal();
    }

}
