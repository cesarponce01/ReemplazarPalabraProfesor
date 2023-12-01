
package com.example.org;

import java.io.*;
import java.util.Scanner;

/**
 @author Jose M.
 Pide una secuencia de caracteres y un fichero de texto por consola.
 Sustituye la secuencia de caracteres por su equivalente en mayusculas.
 Realiza acceso aleatorio al fichero.
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String fichero, palabra, linea;
        int index;

        long numEscrituras = 0L;
        long numSustituciones = 0L;

        StringBuilder sb = new StringBuilder();

        do {
            System.out.println("Nombre del fichero: ");
            fichero = sc.nextLine();
        } while (!new File(fichero).isFile());

        System.out.println("Palabra a cambiar: ");
        palabra = sc.nextLine();

        // Constantes con palabra en mayúscula y su longitud
        final String PAL_MAY = palabra.toUpperCase();
        final int LONG_PALABRA = palabra.length();

        long posFichero = 0;

        long tiempo = System.currentTimeMillis();

        try (RandomAccessFile archivo = new RandomAccessFile(fichero, "rw")) {
            archivo.seek(0);
            while ((linea = archivo.readLine()) != null) {
                sb.setLength(0); // Limpia el sb
                index = linea.indexOf(palabra);
                if (index != -1) {  // Si hay palabra
                    archivo.seek(posFichero);//posición a partir de la cual escribir en fichero

                    // Reemplazos en un sb es mucho más eficiente que con String, por ej. con linea.replaceall(...)
                    sb.append(linea);
                    do {
                        sb.replace(index, index + LONG_PALABRA, PAL_MAY);
                        numSustituciones++;
                    }  while ((index = sb.indexOf(palabra, index + LONG_PALABRA)) != -1);

                    // Graba el string en posición posFichero
                    archivo.writeBytes(sb.toString());
                    numEscrituras++;
                }

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
}