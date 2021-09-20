/*
 * Copyright (c) 2021 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.missiontimerx.fonts;

import javafx.scene.text.Font;


public class Fonts {
    private static final String DIN_BEK_MEDIUM_NAME;
    private static       String dinBekMediumName;


    static {
        try {
            dinBekMediumName = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/missiontimerx/fonts/DINbek Medium.ttf"), 10).getName();
        } catch (Exception exception) { }

        DIN_BEK_MEDIUM_NAME = dinBekMediumName;
    }


    // ******************** Methods *******************************************
    public static Font dinBekMedium(final double size) { return new Font(DIN_BEK_MEDIUM_NAME, size); }
}
