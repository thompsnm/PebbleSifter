package com.androidSifter.sifters.util;

import com.androidSifter.sifters.PebbleSifter;
import com.androidSifter.sifters.exampleSifters.TeamTriviaAnswerSifter;

public class SifterTester {
    public static void main(String [] args)
    {
        // Change sifter to the type you would like to test
        PebbleSifter sifter = new TeamTriviaAnswerSifter();

        System.out.println("Sifter Name: " + sifter.getFullName());
        System.out.println("Sifted Text: " + sifter.sift());
    }
}
