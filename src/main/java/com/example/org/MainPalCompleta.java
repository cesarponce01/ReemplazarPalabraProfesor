
package com.example.org;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 @author Jose M.
 Pide una palabra  y un fichero de texto por consola.
 Sustituye la palabra por su equivalente en mayusculas.
 Realiza acceso aleatorio al fichero.


 NO HACER PUSH ES EL TRABAJO DEL PROFESOR ESTÁ CLONADO
 */
public class MainPalCompleta {
    static long numSustituciones = 0L;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero, palabra, linea;
        int index;

        long numEscrituras = 0L;



        do {
            System.out.println("Nombre del fichero: ");
            fichero = sc.nextLine();
        } while (!new File(fichero).isFile());

        System.out.println("Palabra a cambiar: ");
        palabra = sc.nextLine();

        // Constantes con palabra en mayúscula y su longitud
        final String PAL_MAY = palabra.toUpperCase();
        long posFichero = 0;

        long tiempo = System.currentTimeMillis();

        try (RandomAccessFile archivo = new RandomAccessFile(fichero, "rw")) {
            archivo.seek(0);
            while ((linea = archivo.readLine()) != null) {
                index = linea.indexOf(palabra);
                if (index != -1) {  // Si hay palabra
                    archivo.seek(posFichero);//posición a partir de la cual escribir en fichero
                    linea = reemplazaPalabraMay(linea,palabra,PAL_MAY);
                    // Graba el string en posición posFichero
                    archivo.writeBytes(linea);
                    numEscrituras++;
                } // if

                posFichero=archivo.getFilePointer(); // posición de la siguiente línea
            }
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Error durante la lectura/escritura del fichero");
        }

        System.out.println("Tiempo: " + (System.currentTimeMillis() - tiempo));
        System.out.println("Número de escrituras: " + numEscrituras);
        System.out.println("Número de sustituciones: " + numSustituciones);
    }

    static private String reemplazaPalabraMay(String linea, String palOrig, String palSust){
        StringBuilder sb = new StringBuilder(linea);
        Pattern p = Pattern.compile("\\b"+palOrig+"\\b");  // Crea patrón de búsqueda
        Matcher m = p.matcher(sb);  // Resultado busqueda sobre el sb
        numSustituciones+= m.results().count(); // Número de coincidencias
        return m.replaceAll(palSust);  // Sustitución de las coincidencias en el sb por palSust
    }
}