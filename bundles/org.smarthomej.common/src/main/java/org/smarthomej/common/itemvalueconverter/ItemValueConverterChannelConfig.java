/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 * Copyright (c) 2021 Contributors to the SmartHome/J project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.smarthomej.common.itemvalueconverter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.smarthomej.common.itemvalueconverter.converter.ColorItemConverter;

/**
 * The {@link ItemValueConverterChannelConfig} is an abstract class base class for the channel configuration of things
 * using the item value converters
 *
 * @author Jan N. Klug - Initial contribution
 */

@NonNullByDefault
public class ItemValueConverterChannelConfig {
    private final Map<String, State> stringStateMap = new HashMap<>();
    private final Map<Command, @Nullable String> commandStringMap = new HashMap<>();

    public ChannelMode mode = ChannelMode.READWRITE;

    // number
    public @Nullable String unit;

    // switch, dimmer, color
    public @Nullable String onValue;
    public @Nullable String offValue;

    // dimmer, color
    public BigDecimal step = BigDecimal.ONE;
    public @Nullable String increaseValue;
    public @Nullable String decreaseValue;

    // color
    public ColorItemConverter.ColorMode colorMode = ColorItemConverter.ColorMode.RGB;

    // contact
    public @Nullable String openValue;
    public @Nullable String closedValue;

    // rollershutter
    public @Nullable String upValue;
    public @Nullable String downValue;
    public @Nullable String stopValue;
    public @Nullable String moveValue;

    // player
    public @Nullable String playValue;
    public @Nullable String pauseValue;
    public @Nullable String nextValue;
    public @Nullable String previousValue;
    public @Nullable String rewindValue;
    public @Nullable String fastforwardValue;
    private boolean initialized = false;

    /**
     * maps a command to a user-defined string
     *
     * @param command the command to map
     * @return a string or null if no mapping found
     */
    public @Nullable String commandToFixedValue(Command command) {
        if (!initialized) {
            createMaps();
        }

        return commandStringMap.get(command);
    }

    /**
     * maps a user-defined string to a state
     *
     * @param string the string to map
     * @return the state or null if no mapping found
     */
    public @Nullable State fixedValueToState(String string) {
        if (!initialized) {
            createMaps();
        }

        return stringStateMap.get(string);
    }

    private void createMaps() {
        addToMaps(this.onValue, OnOffType.ON);
        addToMaps(this.offValue, OnOffType.OFF);
        addToMaps(this.openValue, OpenClosedType.OPEN);
        addToMaps(this.closedValue, OpenClosedType.CLOSED);
        addToMaps(this.upValue, UpDownType.UP);
        addToMaps(this.downValue, UpDownType.DOWN);

        commandStringMap.put(IncreaseDecreaseType.INCREASE, increaseValue);
        commandStringMap.put(IncreaseDecreaseType.DECREASE, decreaseValue);
        commandStringMap.put(StopMoveType.STOP, stopValue);
        commandStringMap.put(StopMoveType.MOVE, moveValue);
        commandStringMap.put(PlayPauseType.PLAY, playValue);
        commandStringMap.put(PlayPauseType.PAUSE, pauseValue);
        commandStringMap.put(NextPreviousType.NEXT, nextValue);
        commandStringMap.put(NextPreviousType.PREVIOUS, previousValue);
        commandStringMap.put(RewindFastforwardType.REWIND, rewindValue);
        commandStringMap.put(RewindFastforwardType.FASTFORWARD, fastforwardValue);

        initialized = true;
    }

    private void addToMaps(@Nullable String value, State state) {
        if (value != null) {
            commandStringMap.put((Command) state, value);
            stringStateMap.put(value, state);
        }
    }
}