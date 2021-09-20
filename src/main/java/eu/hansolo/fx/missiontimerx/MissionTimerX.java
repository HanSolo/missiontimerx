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
 import eu.hansolo.fx.missiontimerx.fonts.Fonts;
 import javafx.animation.AnimationTimer;
 import javafx.beans.DefaultProperty;
 import javafx.beans.InvalidationListener;
 import javafx.beans.property.BooleanProperty;
 import javafx.beans.property.BooleanPropertyBase;
 import javafx.beans.property.LongProperty;
 import javafx.beans.property.LongPropertyBase;
 import javafx.beans.property.ObjectProperty;
 import javafx.beans.property.ObjectPropertyBase;
 import javafx.beans.property.ReadOnlyBooleanProperty;
 import javafx.beans.property.StringProperty;
 import javafx.beans.property.StringPropertyBase;
 import javafx.collections.FXCollections;
 import javafx.collections.ListChangeListener;
 import javafx.collections.ObservableList;
 import javafx.geometry.VPos;
 import javafx.scene.Node;
 import javafx.scene.canvas.Canvas;
 import javafx.scene.canvas.GraphicsContext;
 import javafx.scene.layout.Pane;
 import javafx.scene.layout.Region;
 import javafx.scene.paint.Color;
 import javafx.scene.shape.ArcType;
 import javafx.scene.shape.StrokeLineCap;
 import javafx.scene.text.Font;
 import javafx.scene.text.TextAlignment;

 import java.time.Duration;
 import java.util.concurrent.TimeUnit;


 /**
  * User: hansolo
  * Date: 17.09.21
  * Time: 12:31
  */
 @DefaultProperty("children")
 public class MissionTimerX extends Region {
     private static final double                   PREFERRED_WIDTH  = 800;
     private static final double                   PREFERRED_HEIGHT = 164;
     private static final double                   MINIMUM_WIDTH    = 20;
     private static final double                   MINIMUM_HEIGHT   = 20;
     private static final double                   MAXIMUM_WIDTH    = 4096;
     private static final double                   MAXIMUM_HEIGHT   = 4096;
     private static final double                   ASPECT_RATIO     = PREFERRED_HEIGHT / PREFERRED_WIDTH;
     private              double                   width;
     private              double                   height;
     private              Canvas                   canvas;
     private              GraphicsContext          ctx;
     private              Pane                     pane;
     private              double                   outerCircleDiameter;
     private              double                   middleCircleDiameter;
     private              double                   innerCircleDiameter;
     private              double                   centerX;
     private              double                   centerY;
     private              double                   outerOriginX;
     private              double                   outerOriginY;
     private              double                   middleOriginX;
     private              double                   middleOriginY;
     private              double                   innerOriginX;
     private              double                   innerOriginY;
     private              double                   clockFontSize;
     private              double                   titleFontSize;
     private              double                   itemFontSize;
     private              double                   itemDiameter;
     private              double                   itemRadius;
     private              double                   itemLength;
     private              double                   itemDotDiameter;
     private              double                   itemDotRadius;
     private              double                   itemTextDistance;
     private              Font                     clockFont;
     private              Font                     titleFont;
     private              Font                     itemFont;
     private              String                   _title;
     private              StringProperty           title;
     private              Color                    _backgroundColor;
     private              ObjectProperty<Color>    backgroundColor;
     private              Color                    _ringBackgroundColor;
     private              ObjectProperty<Color>    ringBackgroundColor;
     private              Color                    _ringColor;
     private              ObjectProperty<Color>    ringColor;
     private              Color                    _clockColor;
     private              ObjectProperty<Color>    clockColor;
     private              Color                    _titleColor;
     private              ObjectProperty<Color>    titleColor;
     private              Color                    _itemColor;
     private              ObjectProperty<Color>    itemColor;
     private              long                     _startTime;
     private              LongProperty             startTime;
     private              long                     _timeFrame;
     private              LongProperty             timeFrame;
     private              boolean                  _running;
     private              BooleanProperty          running;
     private              ObservableList<Item>     items;
     private              boolean                  itemToggle;
     private              double                   angleStep;
     private              long                     duration;
     private              int                      days;
     private              long                     hours;
     private              long                     minutes;
     private              long                     seconds;
     private              ListChangeListener<Item> itemListener;
     private              InvalidationListener     sizeListener;
     private              long                     lastRedrawCall;
     private              long                     lastTimerCall;
     private              double                   milliAngle;
     private              AnimationTimer           timer;


     // ******************** Constructors **************************************
     public MissionTimerX() {
         this.outerCircleDiameter  = 1.84   * PREFERRED_WIDTH;
         this.middleCircleDiameter = 1.75   * PREFERRED_WIDTH;
         this.innerCircleDiameter  = 1.66   * PREFERRED_WIDTH;
         this.centerX              = 0.5    * PREFERRED_WIDTH;
         this.centerY              = 0.92   * PREFERRED_WIDTH;
         this.outerOriginX         = (PREFERRED_WIDTH - outerCircleDiameter) * 0.5;
         this.outerOriginY         = 0;
         this.middleOriginX        = (PREFERRED_WIDTH - middleCircleDiameter) * 0.5;
         this.middleOriginY        = PREFERRED_HEIGHT - middleCircleDiameter * 0.09142857;
         this.innerOriginX         = (PREFERRED_WIDTH - innerCircleDiameter) * 0.5;
         this.innerOriginY         = PREFERRED_HEIGHT - innerCircleDiameter * 0.06927711;
         this.clockFontSize        = 0.045  * PREFERRED_WIDTH;
         this.titleFontSize        = 0.015  * PREFERRED_WIDTH;
         this.itemFontSize         = 0.0125 * PREFERRED_WIDTH;
         this.itemDiameter         = 0.0125 * PREFERRED_WIDTH;
         this.itemRadius           = itemDiameter * 0.5;
         this.itemLength           = itemDiameter * 0.8;
         this.itemDotDiameter      = 0.005 * PREFERRED_WIDTH;
         this.itemDotRadius        = itemDotDiameter * 0.5;
         this.itemTextDistance     = itemDiameter;
         this.clockFont            = Fonts.dinBekMedium(clockFontSize);
         this.titleFont            = Fonts.dinBekMedium(titleFontSize);
         this.itemFont             = Fonts.dinBekMedium(itemFontSize);
         this._title               = "TITLE";
         this._backgroundColor     = Color.BLACK;
         this._ringBackgroundColor = Color.rgb(15, 15, 15);
         this._ringColor           = Color.rgb(45, 45, 45);
         this._clockColor          = Color.WHITE;
         this._titleColor          = Color.WHITE;
         this._itemColor           = Color.WHITE;
         this._startTime           = 0;
         this._timeFrame           = Duration.ofMinutes(20).getSeconds(); // visible area => 70 degrees
         this._running             = false;
         this.items                = FXCollections.observableArrayList();
         this.itemToggle           = true;
         this.angleStep            = 70.0 / _timeFrame;
         this.duration             = 0;
         this.days                 = 0;
         this.hours                = 0;
         this.minutes              = 0;
         this.seconds              = 0;
         this.itemListener         = c -> {
             for (Item item : items) {
                 item.setUp(itemToggle);
                 itemToggle = !itemToggle;
                 if (getStartTime() + duration >= + item.getTime()) { item.setProcessed(true); }
             }
             redraw();
         };
         this.sizeListener         = o -> resize();
         this.lastRedrawCall       = System.nanoTime();
         this.lastTimerCall        = System.nanoTime();
         this.timer                = new AnimationTimer() {
             @Override public void handle(final long now) {
                 if (now > lastRedrawCall + 1_000_000l) {
                     milliAngle += angleStep / 100.0;
                     redraw();
                     lastRedrawCall = now;
                 }
                 if (now > lastTimerCall + 1_000_000_000l) {
                    long timeSpan = Math.abs((getStartTime() + duration) < 0 ? getStartTime() + duration : (duration + getStartTime()));
                    days    = (int)TimeUnit.SECONDS.toDays(timeSpan);
                    hours   = TimeUnit.SECONDS.toHours(timeSpan)   - (days * 24);
                    minutes = TimeUnit.SECONDS.toMinutes(timeSpan) - (TimeUnit.SECONDS.toHours(timeSpan)   * 60);
                    seconds = TimeUnit.SECONDS.toSeconds(timeSpan) - (TimeUnit.SECONDS.toMinutes(timeSpan) * 60);

                    duration++;
                    milliAngle = 0;
                    //redraw();
                    lastTimerCall = now;
                }
             }
         };

         initGraphics();
         registerListeners();
     }


     // ******************** Initialization ************************************
     private void initGraphics() {
         if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 ||
             Double.compare(getHeight(), 0.0) <= 0) {
             if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                 setPrefSize(getPrefWidth(), getPrefHeight());
             } else {
                 setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
             }
         }

         canvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
         ctx    = canvas.getGraphicsContext2D();

         pane = new Pane(canvas);

         getChildren().setAll(pane);
     }

     private void registerListeners() {
         widthProperty().addListener(sizeListener);
         heightProperty().addListener(sizeListener);
         items.addListener(itemListener);
     }


     // ******************** Methods *******************************************
     @Override protected double computeMinWidth(final double height) { return MINIMUM_WIDTH; }
     @Override protected double computeMinHeight(final double width) { return MINIMUM_HEIGHT; }
     @Override protected double computePrefWidth(final double height) { return super.computePrefWidth(height); }
     @Override protected double computePrefHeight(final double width) { return super.computePrefHeight(width); }
     @Override protected double computeMaxWidth(final double height) { return MAXIMUM_WIDTH; }
     @Override protected double computeMaxHeight(final double width) { return MAXIMUM_HEIGHT; }

     @Override public ObservableList<Node> getChildren() { return super.getChildren(); }

     public String getTitle() { return null == title ? _title : title.get(); }
     public void setTitle(final String title) {
         if (null == this.title) {
             _title = title;
             redraw();
         } else {
             this.title.set(title);
         }
     }
     public StringProperty titleProperty() {
         if (null == title) {
             title = new StringPropertyBase(_title) {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "title"; }
             };
             _title = null;
         }
         return title;
     }

     public Color getBackgroundColor() { return null == backgroundColor ? _backgroundColor : backgroundColor.get(); }
     public void setBackgroundColor(final Color color) {
         if (null == backgroundColor) {
             _backgroundColor = color;
             redraw();
         } else {
             backgroundColor.set(color);
         }
     }
     public ObjectProperty<Color> backgroundColorProperty() {
         if (null == backgroundColor) {
             backgroundColor = new ObjectPropertyBase<>() {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "backgroundColor"; }
             };
             _backgroundColor = null;
         }
         return backgroundColor;
     }

     public Color getRingBackgroundColor() { return null == ringBackgroundColor ? _ringBackgroundColor : ringBackgroundColor.get(); }
     public void setRingBackgroundColor(final Color color) {
         if (null == ringBackgroundColor) {
             _ringBackgroundColor = color;
             redraw();
         } else {
             ringBackgroundColor.set(color);
         }
     }
     public ObjectProperty<Color> ringBackgroundColorProperty() {
         if (null == ringBackgroundColor) {
             ringBackgroundColor = new ObjectPropertyBase<>() {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "ringBackgroundColor"; }
             };
             _ringBackgroundColor = null;
         }
         return ringBackgroundColor;
     }

     public Color getRingColor() { return null == ringColor ? _ringColor : ringColor.get(); }
     public void setRingColor(final Color color) {
         if (null == ringColor) {
             _ringColor = color;
             redraw();
         } else {
             ringColor.set(color);
         }
     }
     public ObjectProperty<Color> ringColorProperty() {
         if (null == ringColor) {
             ringColor = new ObjectPropertyBase<>() {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "ringColor"; }
             };
             _ringColor = null;
         }
         return ringColor;
     }

     public Color getClockColor() { return null == clockColor ? _clockColor : clockColor.get(); }
     public void setClockColor(final Color color) {
         if (null == clockColor) {
             _clockColor = color;
             redraw();
         } else {
             clockColor.set(color);
         }
     }
     public ObjectProperty<Color> clockColorProperty() {
         if (null == clockColor) {
             clockColor = new ObjectPropertyBase<>(_clockColor) {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "clockColor"; }
             };
             _clockColor = null;
         }
         return clockColor;
     }

     public Color getTitleColor() { return null == titleColor ? _titleColor : titleColor.get(); }
     public void setTitleColor(final Color color) {
         if (null == titleColor) {
             _titleColor = color;
             redraw();
         } else {
             titleColor.set(color);
         }
     }
     public ObjectProperty<Color> titleColorProperty() {
         if (null == titleColor) {
             titleColor = new ObjectPropertyBase<>(_titleColor) {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "titleColor"; }
             };
             _titleColor = null;
         }
         return titleColor;
     }

     public Color getItemColor() { return null == itemColor ? _itemColor : itemColor.get(); }
     public void setItemColor(final Color color) {
         if (null == itemColor) {
             _itemColor = color;
             redraw();
         } else {
             itemColor.set(color);
         }
     }
     public ObjectProperty<Color> itemColorProperty() {
         if (null == itemColor) {
             itemColor = new ObjectPropertyBase<>(_itemColor) {
                 @Override protected void invalidated() { redraw(); }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "itemColor"; }
             };
             _itemColor = null;
         }
         return itemColor;
     }

     public long getStartTime() { return null == startTime ? _startTime : startTime.get(); }
     public void setStartTime(final long startTime) {
         if (null == this.startTime) {
             stop();
             _startTime = startTime;
             long timeSpan = Math.abs((getStartTime() + duration) < 0 ? getStartTime() + duration : (duration + getStartTime()));
             days    = (int)TimeUnit.SECONDS.toDays(timeSpan);
             hours   = TimeUnit.SECONDS.toHours(timeSpan)   - (days * 24);
             minutes = TimeUnit.SECONDS.toMinutes(timeSpan) - (TimeUnit.SECONDS.toHours(timeSpan)   * 60);
             seconds = TimeUnit.SECONDS.toSeconds(timeSpan) - (TimeUnit.SECONDS.toMinutes(timeSpan) * 60);
             redraw();
         } else {
             this.startTime.set(startTime);
         }
     }
     public LongProperty startTimeProperty() {
         if (null == startTime) {
             startTime = new LongPropertyBase(_startTime) {
                 @Override protected void invalidated() {
                     stop();
                     redraw();
                 }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "startTime"; }
             };
         }
         return startTime;
     }

     public long getTimeFrame() { return null == timeFrame ? _timeFrame : timeFrame.get(); }
     public void setTimeFrame(final long timeFrame) {
         if (timeFrame < Duration.ofMinutes(5).getSeconds()) { throw new IllegalArgumentException("Minimum timeframe is 5 minutes (" + Duration.ofMinutes(5).getSeconds() + " sec)"); }
         if (null == this.timeFrame) {
            _timeFrame = timeFrame;
             angleStep = 70.0 / _timeFrame;
            redraw();
         } else {
             this.timeFrame.set(timeFrame);
         }
     }
     public LongProperty timeFrameProperty() {
         if (null == timeFrame) {
             timeFrame = new LongPropertyBase(_timeFrame) {
                 @Override protected void invalidated() {
                     angleStep = 70.0 / get();
                     redraw();
                 }
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "timeFrame"; }
             };
         }
         return timeFrame;
     }

     public boolean isRunning() { return null == running ? _running : running.get(); }
     private final void setRunning(final boolean running) {
         if (null == this.running) {
             _running = running;
         } else {
             this.running.set(running);
         }
     }
     public ReadOnlyBooleanProperty runningProperty() {
         if (null == running) {
             running = new BooleanPropertyBase(_running) {
                 @Override public Object getBean() { return MissionTimerX.this; }
                 @Override public String getName() { return "running"; }
             };
         }
         return running;
     }

     public ObservableList<Item> getItems() { return items; }

     public void start() {
        if (isRunning()) { return; }
        duration = 0;
        setRunning(true);
         timer.start();
     }
     public void pause() {
         stop();
     }
     public void resume() {
         if (isRunning()) { return; }
         setRunning(true);
         timer.start();
     }
     public void stop() {
        if (!isRunning()) { return; }
        setRunning(false);
         timer.stop();
     }

     public void dispose() {
         widthProperty().removeListener(sizeListener);
         heightProperty().removeListener(sizeListener);
         items.removeListener(itemListener);
     }


     // ******************** Layout *******************************************
     private void resize() {
         width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
         height = getHeight() - getInsets().getTop() - getInsets().getBottom();

         if (ASPECT_RATIO * width > height) {
             width = 1 / (ASPECT_RATIO / height);
         } else if (1 / (ASPECT_RATIO / height) > width) {
             height = ASPECT_RATIO * width;
         }

         if (width > 0 && height > 0) {
             canvas.setWidth(width);
             canvas.setHeight(height);

             pane.setMaxSize(width, height);
             pane.setPrefSize(width, height);
             pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

             this.outerCircleDiameter  = 1.84 * width;
             this.middleCircleDiameter = 1.75 * width;
             this.innerCircleDiameter  = 1.66 * width;
             this.centerX              = 0.50 * width;
             this.centerY              = outerCircleDiameter * 0.5;
             this.outerOriginX         = (width - outerCircleDiameter) * 0.5;
             this.outerOriginY         = 0;
             this.middleOriginX        = (width - middleCircleDiameter) * 0.5;
             this.middleOriginY        = height - middleCircleDiameter * 0.09142857;
             this.innerOriginX         = (width - innerCircleDiameter) * 0.5;
             this.innerOriginY         = height - innerCircleDiameter * 0.06927711;
             this.clockFontSize        = 0.045  * width;
             this.titleFontSize        = 0.015  * width;
             this.itemFontSize         = 0.012  * width;
             this.itemDiameter         = 0.0125 * width;
             this.itemRadius           = 0.5 * itemDiameter;
             this.itemLength           = itemDiameter * 0.85;
             this.itemDotDiameter      = 0.005 * width;
             this.itemDotRadius        = itemDotDiameter * 0.5;
             this.itemTextDistance     = itemDiameter;
             this.clockFont            = Fonts.dinBekMedium(clockFontSize);
             this.titleFont            = Fonts.dinBekMedium(titleFontSize);
             this.itemFont             = Fonts.dinBekMedium(itemFontSize);
             redraw();
         }
     }

     private void redraw() {
         ctx.clearRect(0, 0, width, height);

         ctx.setFill(getRingBackgroundColor());
         ctx.fillOval(outerOriginX, outerOriginY, outerCircleDiameter, outerCircleDiameter);

         ctx.setStroke(getRingColor());
         ctx.setLineWidth(width * 0.0025);
         ctx.strokeOval(middleOriginX, middleOriginY, middleCircleDiameter, middleCircleDiameter);

         ctx.setFill(getBackgroundColor());
         ctx.fillOval(innerOriginX, innerOriginY, innerCircleDiameter, innerCircleDiameter);

         ctx.setLineWidth(width * 0.0025);
         ctx.strokeOval(innerOriginX, innerOriginY, innerCircleDiameter, innerCircleDiameter);

         ctx.setStroke(getItemColor());
         ctx.strokeArc(middleOriginX, middleOriginY, middleCircleDiameter, middleCircleDiameter, 90, 90, ArcType.OPEN);
         ctx.strokeLine(centerX, middleOriginY - width * 0.0025, centerX, middleOriginY + width * 0.0025);

         ctx.setTextAlign(TextAlignment.CENTER);
         ctx.setTextBaseline(VPos.CENTER);

         ctx.setFill(getClockColor());
         ctx.setFont(clockFont);
         ctx.fillText(new StringBuilder().append("T ").append(getStartTime() + duration > 0 ? "+" : "-").append(" ").append(0 == days ? "" : days).append(0 == days ? "" : ":").append(String.format("%02d", hours)).append(":").append(String.format("%02d", minutes)).append(":").append(0 == days ? String.format("%02d", seconds) : "").toString(), centerX, height * 0.6804878);

         ctx.setFill(getTitleColor());
         ctx.setFont(titleFont);
         ctx.fillText(getTitle(), centerX, height * 0.83658537);

         ctx.setFont(itemFont);
         items.forEach(item -> {
             double itemAngle = -getStartTime() * angleStep + (item.getTime() * angleStep) - duration * angleStep - milliAngle;
             if (itemAngle > -35 && itemAngle < 35) {
                 ctx.save();

                 ctx.translate(centerX, centerY);
                 ctx.rotate(itemAngle);
                 ctx.translate(-centerX, -centerY);

                 ctx.setLineWidth(0.0015 * width);
                 ctx.setFill(getRingBackgroundColor());
                 ctx.fillOval(centerX - itemRadius, middleOriginY - itemRadius, itemDiameter, itemDiameter);

                 Color itemColor;
                 if (itemAngle < -24) {
                     itemColor = getItemColor().darker().darker();
                 } else if (itemAngle < -12) {
                     itemColor = getItemColor().darker().darker();
                 } else if (itemAngle < 12) {
                     itemColor = getItemColor();
                 } else if (itemAngle < 24) {
                     itemColor = getItemColor().darker().darker();
                 } else {
                     itemColor = getItemColor().darker().darker().darker().darker();
                 }

                 ctx.setLineCap(StrokeLineCap.ROUND);
                 ctx.setStroke(itemColor);
                 ctx.setFill(itemColor);
                 if (item.isUp()) {
                     ctx.strokeLine(centerX, middleOriginY - itemRadius, centerX, middleOriginY - itemLength);
                     ctx.setTextBaseline(VPos.BOTTOM);
                     ctx.fillText(item.getName(), centerX, middleOriginY - itemTextDistance);
                 } else {
                     ctx.strokeLine(centerX, middleOriginY + itemRadius, centerX, middleOriginY + itemLength);
                     ctx.setTextBaseline(VPos.TOP);
                     ctx.fillText(item.getName(), centerX, middleOriginY + itemTextDistance);
                 }

                 ctx.setStroke(itemColor);
                 ctx.strokeOval(centerX - itemRadius, middleOriginY - itemRadius, itemDiameter, itemDiameter);

                 if (getStartTime() + (duration - 1) >= + item.getTime()) {
                     if (!item.isProcessed()) {
                         item.setProcessed(true);
                         fireEvent(new MissionTimerXEvent(item, MissionTimerX.this, null, MissionTimerXEvent.TRIGGERED));
                     }
                     ctx.setFill(getItemColor());
                     ctx.fillOval(centerX - itemDotRadius, middleOriginY - itemDotRadius, itemDotDiameter, itemDotDiameter);
                 }

                 ctx.restore();
             }
         });
     }
 }
