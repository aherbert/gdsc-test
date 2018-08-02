/*-
 * #%L
 * Genome Damage and Stability Centre Test Package
 *
 * The GDSC Test package contains code for use with the JUnit test framework.
 * %%
 * Copyright (C) 2018 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package uk.ac.sussex.gdsc.test;

/**
 * Defines a named task that was run.
 * <p>
 * This class is used for reporting and has no effect when passed to {@link TimingService#execute(TimingTask)}.
 */
public class NamedTimingTask extends BaseTimingTask
{
    /**
     * Instantiates a new named timing task.
     *
     * @param name
     *            the name
     */
    public NamedTimingTask(String name)
    {
        super(name);
    }

    @Override
    public int getSize()
    {
        return 0;
    }

    @Override
    public Object getData(int i)
    {
        return null;
    }

    @Override
    public Object run(Object data)
    {
        return null;
    }
}
