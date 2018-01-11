package servidorweb;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ServidorWeb {

    Socket cliente;
    
    private static String Status(String arquivo){
        String base = "/home/gabriell/NetBeansProjects/ServidorWeb/src/servidorweb/www/";
        
        File pagina = new File(base+arquivo);
            
        if(pagina.exists()){
            return "200 OK";
        }else{
            return "404 Not Found";
        }
    }
    
    private static String LerArquivo(String arquivo){
        String base = "/home/gabriell/NetBeansProjects/ServidorWeb/src/servidorweb/www/";
        String linha = "";
        
        try{
            File pagina = new File(base+arquivo);
            
            if(pagina.exists()){
                String temp = arquivo.replace(".", "#");
                String[] extensao = temp.split("#");
                if(extensao[1].equals("php")){
                    final ProcessBuilder processBuilder = new ProcessBuilder();
                    processBuilder.command("/usr/bin/php", base + arquivo);
                    final Process process = processBuilder.start();    
                    process.getInputStream().read();
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    linha = "<";
                    while(br.ready()){
                        linha += br.readLine();
                    }
                    br.close();
                    process.destroy();
                }else{
                    BufferedReader br = new BufferedReader(new FileReader(base + arquivo));
                    while(br.ready()){
                        linha += br.readLine();
                    } 
                    br.close();
                }
                
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
    
    private static String CriaResposta(String arquivo, String contentType) {
        
        SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
        formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date data = new Date();
        String dataFormatada = formatador.format(data) + " GMT";
        String status = Status(arquivo);
        
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(status).append("\r\n");
        sb.append("Date: ").append(dataFormatada).append("\r\n");
        sb.append("Server: Test Server").append("\r\n");
        sb.append("Connection: Close").append("\r\n");
        sb.append("Content-Type: ").append(contentType).append("; charset=UTF-8").append("\r\n");
        sb.append("\r\n");
        
        String respostaArquivo = LerArquivo(arquivo);
        sb.append(respostaArquivo);
        
        return sb.toString();
    }
    
    
    public static void main(String[] args){
            try {
            ServerSocket servidor = new ServerSocket(8000);
            System.out.println("Servidor iniciado! Acesse localhost:8000 e veja");
            while (true) {
                Socket cliente = servidor.accept();
                
                new Thread(() -> {
                    
                    try{

                        BufferedReader buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        String leitura = buffer.readLine();
                        String[] dadosReq = leitura.split(" ");
                        String caminhoArquivo = dadosReq[1];
                        String arquivo = "";
                        String contentType =  "";
                        
                        if(caminhoArquivo.equals("/")){
                            arquivo = "index.html";
                            contentType = "text/html";
                        }else{
                            String[] quebraCaminho = caminhoArquivo.split("/");
                            arquivo = quebraCaminho[1];
                            try{
                                String temp = arquivo.replace(".", "#");
                                String[] extensao = temp.split("#");

                                if(extensao[1].equals("css")){
                                    contentType = "text/css";
                                }else{
                                    contentType = "text/html";
                                }
                            }catch(Exception e){
                                contentType = "text/html";
                            }
                            
                        }
                        
                        String resposta = CriaResposta(arquivo, contentType);
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