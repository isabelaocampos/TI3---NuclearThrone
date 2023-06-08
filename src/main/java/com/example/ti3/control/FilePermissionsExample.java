package com.example.ti3.control;

import java.io.File;

public class FilePermissionsExample {
    public static void main(String[] args) {
        File file = new File("animations/hero/idle/idle_01.png\"");

        System.out.println(file.getAbsolutePath());

        if (file.exists()) {
            if (file.canRead()) {
                System.out.println("El archivo es legible");
            } else {
                System.out.println("No se tienen permisos de lectura para el archivo");
            }
        } else {
            System.out.println("El archivo no existe");
        }
    }
}
