package ru.urlShortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * To shorten long decimals via converting to 62 base system. E.g decimal value 62 will be 'Z' and vise versa
 */
public class DecimalLetterCoder {
    private String symbols = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private char[] c = symbols.toCharArray();
    private int base = c.length;

    public DecimalLetterCoder() {
    }

    /**
     * Converts decimal positive value to 62 base value
     * @param dec decimal positive value.
     * @return 62 base value. If error return empty string.
     */
    public String decToStr(long dec) {
        int reminder;
        long quotient = dec;
        String result = "";
        StringBuilder str = new StringBuilder();
        if (dec >= 0) {
            do {
                reminder = (int)(quotient % base);
                quotient /= base;
                str.append(c[reminder]);
            } while (quotient > 0);
            result = str.reverse().toString();
        }
        return result;
    }

    /**
     * Converts string value of 62 base to decimal.
     *
     * @param str string value of 62 base
     * @return converted to positive decimal value. If error returns -1;
     */
    public long strToDec(String str) {
        long result = 0;
        str = new StringBuilder(str.trim()).reverse().toString();

        int symbolIndex = 0;
        if (str.length() > 0) {
            for(int i = 0; i < str.length(); i++) {
                symbolIndex = symbols.indexOf(str.charAt(i));
                if (symbolIndex == -1) {
                    result = -1;
                    break;
                }
                result += symbolIndex * Math.pow(base, i);
            }
        } else {
            result = -1;
        }
        return result;
    }
}
