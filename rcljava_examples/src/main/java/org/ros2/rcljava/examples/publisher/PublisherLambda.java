/* Copyright 2017 Esteve Fernandez <esteve@apache.org>
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

package org.ros2.rcljava.examples.publisher;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.concurrent.Callback;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.publisher.Publisher;
import org.ros2.rcljava.timer.WallTimer;

public class PublisherLambda extends BaseComposableNode {
  private int count;
  //Decl 2 publishers
  private Publisher<std_msgs.msg.String> publisher1;
  private Publisher<std_msgs.msg.String> publisher2;

  private WallTimer timer;

  public PublisherLambda() {
    super("minmal_publisher");
    this.count = 0;
    // Publishers are type safe, make sure to pass the message type
    // Assign ros2 msg data type and topic name to publishers
    this.publisher1 = node.<std_msgs.msg.String>createPublisher(std_msgs.msg.String.class, "topic1");
    this.publisher2 = node.<std_msgs.msg.String>createPublisher(std_msgs.msg.String.class, "topic2");

    //Publish msg on 2 topics
    Callback timerCallback = () -> {
      std_msgs.msg.String msg1 = new std_msgs.msg.String();
      std_msgs.msg.String msg2 = new std_msgs.msg.String();
      msg1.setData("Hello Earth, for " + this.count + "th time!");
      msg2.setData("Hello Mars, for " + (this.count+5) + "th time!");
      this.count+=10;

      System.out.println("Publisher_1: [" + msg1.getData() + "]");
      this.publisher1.publish(msg1);

      System.out.println("Publisher_2: [" + msg2.getData() + "]");
      this.publisher2.publish(msg2);
    };
    this.timer = node.createWallTimer(500, TimeUnit.MILLISECONDS, timerCallback);
  }

  public static void main(String[] args) throws InterruptedException {
    // Initialize RCL
    RCLJava.rclJavaInit();

    RCLJava.spin(new PublisherLambda());
  }
}