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
package org.chocosolver.parser.flatzinc.ast.expression;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

/*
* User : CPRUDHOM
* Mail : cprudhom(a)emn.fr
* Date : 11 janv. 2010
* Since : Choco 2.1.1
*
* Class for boolean expressions definition based on flatzinc-like objects.
*/
public final class EBool extends Expression {

    public final boolean value;

    public static final EBool instanceTrue = new EBool(true);
    public static final EBool instanceFalse = new EBool(false);

    public static EBool make(boolean value) {
        if (value) return instanceTrue;
        else return instanceFalse;
    }

    private EBool(boolean value) {
        super(EType.BOO);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int intValue() {
        return value ? 1 : 0;
    }

    @Override
    public int[] toIntArray() {
        return new int[]{intValue()};
    }

    @Override
    public boolean boolValue() {
        return value;
    }

    @Override
    public boolean[] toBoolArray() {
        return new boolean[]{boolValue()};
    }

    @Override
    public BoolVar boolVarValue(Model model) {
        return intValue() == 1 ? model.boolVar(true) : model.boolVar(false);
    }

    @Override
    public BoolVar[] toBoolVarArray(Model model) {
        return new BoolVar[]{boolVarValue(model)};
    }

    @Override
    public IntVar intVarValue(Model model) {
        return model.intVar(intValue());
    }

    @Override
    public IntVar[] toIntVarArray(Model solver) {
        return new IntVar[]{intVarValue(solver)};
    }

    @Override
    public int[][] toIntMatrix() {
        return new int[][]{{intValue()}};
    }
}
