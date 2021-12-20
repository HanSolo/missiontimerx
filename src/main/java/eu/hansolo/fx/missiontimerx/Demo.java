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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Demo extends Application {
    private static int           noOfNodes = 0;
    private        MissionTimerX missionTimerX;


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

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println(noOfNodes + " Nodes in SceneGraph");

        missionTimerX.start();
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> keyReleased(e));        
    }
    /**
     * Key handler for various keyboard shortcuts
     * 
     */    
    private void keyReleased(KeyEvent e) {
        if(e.getCode().equals(KeyCode.LEFT)) {
            missionTimerX.decreaseTimelineSpeed();
            missionTimerX.resume();
        }
        if(e.getCode().equals(KeyCode.RIGHT)) {
            missionTimerX.increaseTimelineSpeed();
            missionTimerX.resume();
        }
        if(e.getCode().equals(KeyCode.ENTER)) {
            missionTimerX.resetTimelineSpeed();
            missionTimerX.resume();
        }
        if(e.getCode().equals(KeyCode.SPACE)) {
            if(missionTimerX.isRunning())
                missionTimerX.pause();
            else
                missionTimerX.resume();
        }
    }

    @Override public void stop() {

    }

    private static void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
