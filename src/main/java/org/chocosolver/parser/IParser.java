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
package org.chocosolver.parser;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Settings;

/**
 * An interface for all parsers
 * <br/>
 *
 * @author Charles Prud'homme
 * @version choco-parsers
 * @since 21/10/2014
 */
public interface IParser {

    /**
     * Add a parser listener
     *
     * @param listener
     */
    void addListener(ParserListener listener);

    /**
     * Remove a parser listener
     *
     * @param listener
     */
    void removeListener(ParserListener listener);

    /**
     * Parse the program arguments
     *
     * @param args program arguments
     */
    void parseParameters(String[] args);


    /**
     * Declare the settings to use
     *
     * @param defaultSettings settings to consider
     */
    void defineSettings(Settings defaultSettings);

    /**
     * Create the solver
     */
    void createSolver();

    /**
     * Parse the file
     */
    void parseInputFile() throws Exception;

    /**
     * Configure the search strategy
     */
    void configureSearch();

    /**
     * Run the resolution of the given solver
     */
    void solve();

    /**
     * @return a thread to execute on unexpected exit
     */
    Thread actionOnKill();

    /**
     * Get the solver
     *
     * @return solver
     */
    Model getModel();
}
