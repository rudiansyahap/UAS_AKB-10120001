/**
 * NIM : 10120001
 * NAMA : Rudiansyah Adi Prasetyo
 * KELAS : IF-1
 */
package com.uas.notes.helper;

public class StringHelper {
    // Generate Username From Email
    public static String usernameFromEmail(java.lang.String email) {
        if (!email.contains("@")) return email;

        return email.split("@")[0];
    }
}
