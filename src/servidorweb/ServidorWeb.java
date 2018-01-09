package servidorweb;
import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorWeb {

    Socket cliente;
    
    private static String LerArquivo(){
        String linha = "";
        
        try{
            String base = "/home/gabriell/NetBeansProjects/ServidorWeb/src/servidorweb/"; 
            BufferedReader br = new BufferedReader(new FileReader(base + "index.html"));
            while(br.ready()){
                linha = br.readLine();
            }   
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return linha;
    }
    
    private static String CriaResposta() {
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK").append("\r\n");
        sb.append("Date: ").append(new Date()).append("\r\n");
        sb.append("Server: Test Server").append("\r\n");
        sb.append("Connection: Close").append("\r\n");
        sb.append("Content-Type: text/html; charset=UTF-8").append("\r\n");
        sb.append("\r\n");
        
        String respostaArquivo = LerArquivo();
        sb.append(respostaArquivo);
        
        
//        sb.append("<html><head><title>Servidor web</title></head><body><h1>HttpServer Response</h1>");
//        sb.append("Method: ").append("GET").append("<br/>");
//        sb.append("URI: ").append("/").append("<br/>");
//        sb.append("Protocol: ").append("HTTP/1.1").append("<br/>");
//        sb.append("</body></html>");

//        if()
        return sb.toString();
    }
    
    
    public static void main(String[] args){
            try {
            ServerSocket servidor = new ServerSocket(8000);
            while (true) {
                Socket cliente = servidor.accept();
                
                new Thread(() -> {
                    
                    try{
                        
                        System.out.println("Nova conexão com o cliente "
                                           + cliente.getInetAddress().
                                            getHostAddress());
                        System.out.println("Hostname "
                                           + cliente.getInetAddress().
                                            getHostName());
                        
                        String request = cliente.getInputStream().toString();
                        System.out.println(request);
                        String resposta = CriaResposta();
                        cliente.getOutputStream().write(resposta.getBytes());
                        cliente.close();
                        
                        
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    
                }).start();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}