package com.xj.queue;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.xj.util.ActiveMQUtil;
import cn.hutool.core.util.RandomUtil;
/**
 * 订阅者
 * @author root
 *
 *
 * activeMQ 有两种模式，分别是队列模式和主题模式。
队列模式，其实就是分食模式。 比如生产方发了 10条消息到 activeMQ 服务器， 而此时有多个 消费方，那么这些消费方就会瓜分这些10条消息，一条消息只会被一个消费方得到。
主题模式，就是订阅模式。 比如生产方发了10条消息，而此时有多个消费方，那么多个消费方都能得到这 10条消息，就如同订阅公众号那样。
 */
public class testConsumer {
    //服务地址，端口默认61616
    private static final String url="tcp://127.0.0.1:61616";
    //这次消费的消息名称
    private static final String topicName="queue_style";

    //消费者有可能是多个，为了区分不同的消费者，为其创建随机名称
    private static final String consumerName="consumer-" + RandomUtil.randomString(5);
    public static void main(String[] args) throws JMSException {
        //0. 先判断端口是否启动了 Active MQ 服务器
        ActiveMQUtil.checkServer();
        System.out.printf("%s 消费者启动了。 %n", consumerName);

        //1.创建ConnectiongFactory,绑定地址
        ConnectionFactory factory=new ActiveMQConnectionFactory(url);
        //2.创建Connection
        Connection connection= factory.createConnection();
        //3.启动连接
        connection.start();
        //4.创建会话
        Session session=connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        //5.创建一个目标 （主题类型）
//        Destination destination=session.createQueue(topicName);

        //5.创建一个目标 (主题类型)
        Destination destination=session.createTopic(topicName);
        //6.创建一个消费者
        MessageConsumer consumer=session.createConsumer(destination);
        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {

            public void onMessage(Message arg0) {
                // TODO Auto-generated method stub
                TextMessage textMessage=(TextMessage)arg0;
                try {
                    System.out.println(consumerName +" 接收消息："+textMessage.getText());
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        //8. 因为不知道什么时候有，所以没法主动关闭，就不关闭了，一直处于监听状态
        //connection.close();
    }
}