package com.example.org;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero, palabra, linea;
        int numEscrituras = 0;
        int numSustituciones = 0;
        long pos = 0;
        StringBuilder sb = new StringBuilder();
        boolean haySusti;
        int index;

        do {
            System.out.println("Nombre del fichero: ");
            fichero = sc.nextLine();
        } while (!new File(fichero).isFile());

        System.out.println("Palabra a cambiar: ");
        palabra = sc.nextLine();

        final String PAL_MAY = palabra.toUpperCase();
        final int LONG_PALABRA = palabra.length();

        long tiempo = System.currentTimeMillis();

        try (RandomAccessFile archivo = new RandomAccessFile(fichero, "rw")) {
            archivo.seek(pos);
            while ((linea = archivo.readLine()) != null) {
                sb.setLength(0); // Limpia el sb
                index = linea.indexOf(palabra);
                haySusti=false;
                if (index != -1) {  // Si hay palabra
                    haySusti=true;
                    numSustituciones++;
                    archivo.seek(pos + index);//posición a partir de la cual escribir en fichero
                    // Mete en sb PALABRA+resto de la linea. No mete el texto anterior a la palabra
                    sb.append(PAL_MAY);
                    sb.append(linea.substring(index + LONG_PALABRA));
                    //Sigue explorando resto de la línea y sustituyendo sobre el sb
                    index=0;
                    while ((index = sb.indexOf(palabra, index + LONG_PALABRA)) != -1) {
                        sb.replace(index, index + LONG_PALABRA, PAL_MAY);
                        numSustituciones++;
                    }
                    archivo.writeBytes(sb.toString());
                    numEscrituras++;
                }
                // Posiciona en siguiente línea a leer
                pos = haySusti ? archivo.getFilePointer() + 1 : archivo.getFilePointer();
                archivo.seek(pos);
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
}