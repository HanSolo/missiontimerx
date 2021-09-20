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

package eu.hansolo.fx.missiontimerx;

import eu.hansolo.fx.missiontimerx.events.MissionTimerXEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.Duration;


public class Demo extends Application {
    private MissionTimerX missionTimerX;


    @Override public void init() {

        missionTimerX = MissionTimerXBuilder.create()
                                            .title("INSPIRATION4")
                                            .startTime(-Duration.ofMinutes(5).getSeconds())
                                            .timeFrame(Duration.ofMinutes(20).getSeconds())
                                            .items(new Item(-Duration.ofMinutes(6).getSeconds(), "ENGINE CHILL"),
                                                   new Item(-Duration.ofMinutes(3).getSeconds(), "STRONGBACK\nRETRACT"),
                                                   new Item(-Duration.ofMinutes(0).getSeconds(), "LIFTOFF"),
                                                   new Item(Duration.ofMinutes(5).getSeconds(), "MAX-Q"))
                                            .build();

        registerListeners();
    }

    private void registerListeners() {
        missionTimerX.addEventHandler(MissionTimerXEvent.TRIGGERED, e -> System.out.println("Item: " + e.getItem().getName() + " processed"));
    }

    @Override public void start(final Stage stage) {
        StackPane pane = new StackPane(missionTimerX);

        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle("MissionTimerX");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        missionTimerX.start();
    }

    @Override public void stop() {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
