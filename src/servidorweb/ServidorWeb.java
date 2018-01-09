package servidorweb;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServidorWeb {

    Socket cliente;
    
    private static String LerArquivo(String arquivo){
        String base = "/home/gabriell/NetBeansProjects/ServidorWeb/src/servidorweb/www/";
        String linha = "";
        
        try{
            File pagina = new File(base+arquivo);
            
            if(pagina.exists()){
                BufferedReader br = new BufferedReader(new FileReader(base + arquivo));
                while(br.ready()){
                    linha += br.readLine();
                } 
                br.close();
            }else{
                BufferedReader br = new BufferedReader(new FileReader(base + "404.html"));
                while(br.ready()){
                    linha += br.readLine();
                } 
                br.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
        return linha;
    }
    
    private static String CriaResposta(String arquivo) {
        
        SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date data = new Date();
        //Formata a dara para o padrao
        String dataFormatada = formatador.format(data) + " GMT";
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK").append("\r\n");
        sb.append("Date: ").append(dataFormatada).append("\r\n");
        sb.append("Server: Test Server").append("\r\n");
        sb.append("Connection: Close").append("\r\n");
        sb.append("Content-Type: text/html; charset=UTF-8").append("\r\n");
        sb.append("\r\n");
        
        String respostaArquivo = LerArquivo(arquivo);
        sb.append(respostaArquivo);
        
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
                       
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        String leitura = buffer.readLine();
                        String[] dadosReq = leitura.split(" ");
                        String caminhoArquivo = dadosReq[1];
                        String arquivo = "";
                        
                        
                        if(caminhoArquivo.equals("/")){
                            arquivo = "index.html";
                        }else{
                            String[] quebraCaminho = caminhoArquivo.split("/");
                            arquivo = quebraCaminho[1];
                        }
                        
                        String resposta = CriaResposta(arquivo);
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