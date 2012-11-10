
package jots.login;

import static org.jboss.netty.channel.Channels.*;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

public class LoginServerPipelineFactory implements ChannelPipelineFactory {

    
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("handler", new LoginServerHandler());
        return pipeline;
    }
    
}
