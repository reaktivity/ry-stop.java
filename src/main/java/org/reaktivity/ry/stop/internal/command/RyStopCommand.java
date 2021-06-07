/**
 * Copyright 2016-2021 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.ry.stop.internal.command;

import static java.nio.ByteOrder.nativeOrder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.reaktivity.ry.RyCommand;

import com.github.rvesse.airline.annotations.Command;

@Command(name = "stop", description = "Stop engine")
public final class RyStopCommand extends RyCommand
{
    private final Path directory;

    public RyStopCommand()
    {
        this.directory = Paths.get(".ry", "engine");
    }

    @Override
    public void run()
    {
        Path info = directory.resolve("info");

        try
        {
            if (Files.exists(info))
            {
                ByteBuffer byteBuf = ByteBuffer
                        .wrap(Files.readAllBytes(info))
                        .order(nativeOrder());

                long pid = byteBuf.getLong(0);
                ProcessHandle.of(pid)
                    .ifPresent(ProcessHandle::destroy);
            }
        }
        catch (IOException ex)
        {
            System.out.printf("Error: %s is not readable\n", info);
        }
    }
}
