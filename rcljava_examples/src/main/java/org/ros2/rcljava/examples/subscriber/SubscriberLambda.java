/* Copyright 2016-2017 Esteve Fernandez <esteve@apache.org>
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

package org.ros2.rcljava.examples.subscriber;

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.concurrent.Callback;

import org.ros2.rcljava.consumers.Consumer;
import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.subscription.Subscription;

import org.ros2.rcljava.publisher.Publisher;
import org.ros2.rcljava.timer.WallTimer;


public class SubscriberLambda extends BaseComposableNode {
  //Decl. 2 subscriptions and one publisher
  private Subscription<std_msgs.msg.String> sub1;
  private Subscription<std_msgs.msg.String> sub2;
  private Publisher<std_msgs.msg.String> pub;

  //vars to retain topic data for use in publisher
  private int pub1_val, pub2_val;


  private WallTimer timer;

  public SubscriberLambda() {
    super("minimal_subscriber");
    
    pub1_val = pub2_val = 0;  //init topic int data retention vars
    
    //Set subscription topic type and name
    sub1 = node.<std_msgs.msg.String>createSubscription(std_msgs.msg.String.class, "topic1", this::topicCallback1);
    sub2 = node.<std_msgs.msg.String>createSubscription(std_msgs.msg.String.class, "topic2", this::topicCallback2);

    //Assign topic type and name to publisher
    this.pub = node.<std_msgs.msg.String>createPublisher(std_msgs.msg.String.class, "addition");
    
    //Publish addition of data from 2 subscribed topics on addition topic
    Callback timerCallback = () -> {
      std_msgs.msg.String msg = new std_msgs.msg.String();
    
      msg.setData("total greetings: "+ (pub1_val + pub2_val) );
      System.out.println("Publisher_add: [" + msg.getData() + "]");
      this.pub.publish(msg);
    };
    this.timer = node.createWallTimer(500, TimeUnit.MILLISECONDS, timerCallback);
  }

  //Extract the published number from the msg string for topic 1
  private void topicCallback1(final std_msgs.msg.String msg1) {
    System.out.println("I heard: [" + msg1.getData() + "]");
    pub1_val =  Integer.parseInt(msg1.getData().replaceAll("[^0-9]", ""));
  }

  ////Extract the published number from the msg string for topic 2
  private void topicCallback2(final std_msgs.msg.String msg2) {
    System.out.println("I heard: [" + msg2.getData() + "]");
    pub2_val =  Integer.parseInt(msg2.getData().replaceAll("[^0-9]", ""));
  }


  public static void main(final String[] args) throws InterruptedException, Exception {
    // Initialize RCL
    RCLJava.rclJavaInit();

    RCLJava.spin(new SubscriberLambda());
  }
}
