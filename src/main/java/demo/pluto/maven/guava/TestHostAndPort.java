package demo.pluto.maven.guava;

import com.google.common.net.HostAndPort;

public class TestHostAndPort {

    public static void main(String[] args) {
        HostAndPort  hostAndPort =HostAndPort.fromString("127.0.0.1:8080");
        System.out.println("host== " + hostAndPort.getHost());
        System.out.println("port== " + hostAndPort.getPortOrDefault(80));

    }

}
