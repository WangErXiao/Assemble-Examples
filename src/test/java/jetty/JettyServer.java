package jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.StdErrLog;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 */
public class JettyServer {

    /** port */
    private int port = 9999;

    protected String charset = "UTF-8";

    public JettyServer() {
    }

    public JettyServer(int port) {
        this.port = port;
    }

    public JettyServer(int port, String charset) {
        super();
        this.port = port;
        this.charset = charset;
    }

    /**
     * 服务器启动。
     * 
     * @throws javax.naming.ConfigurationException
     */
    public void start() {

        // 设置Jetty日志
        System.setProperty("org.eclipse.jetty.util.log.class", StdErrLog.class.getName());

        Server server = new Server();

        // 设置连接器
        Connector connector = new SelectChannelConnector();
        connector.setPort(this.port);
        connector.setRequestHeaderSize(16246);
        server.setConnectors(new Connector[] { connector });

        // 设置context
        WebAppContext context = new WebAppContext();
        
        context.setContextPath("/");
        //idea
        context.setResourceBase("./src/main/webapp");
        context.setDefaultsDescriptor("./src/test/java/jetty/webdefault.xml");
       context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setParentLoaderPriority(true);
        server.setHandler(context);

        // 启动Server
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        JettyServer peckerServer = new JettyServer();

        peckerServer.start();
    }
}