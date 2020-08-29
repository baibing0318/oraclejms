package org.kevin.jms;
 
import java.util.Properties;
 
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
 
import oracle.jms.AQjmsAdtMessage;
import oracle.jms.AQjmsDestination;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;
 
/**
 * 
 * 消息处理类
 *
 */
public class Main {
 
    public static void main(String[] args) throws Exception {
        JmsConfig config = new JmsConfig();
 
        QueueConnectionFactory queueConnectionFactory = AQjmsFactory.getQueueConnectionFactory(config.jdbcUrl,
                new Properties());
 
        QueueConnection conn = queueConnectionFactory.createQueueConnection(config.username, config.password);
        AQjmsSession session = (AQjmsSession) conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
 
        conn.start();
 
        Queue queue = (AQjmsDestination) session.getQueue(config.username, config.queueName);
        MessageConsumer consumer = session.createConsumer(queue, null, QUEUE_MESSAGE_TYPE.getFactory(), null, false);
 
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
       
                AQjmsAdtMessage adtMessage = (AQjmsAdtMessage) message;
 
                try {
                    QUEUE_MESSAGE_TYPE payload = (QUEUE_MESSAGE_TYPE) adtMessage.getAdtPayload();
                    System.out.println(payload.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
 
        Thread.sleep(1000000);
    }
 
}