package com.pebbleSifter.sifters.util;

import com.pebbleSifter.sifters.PebbleSifter;
import com.pebbleSifter.sifters.exampleSifters.TeamTriviaAnswerSifter;

public class SifterTester {
    public static void main(String [] args)
    {
        // Change sifter to the type you would like to test
        PebbleSifter sifter = new TeamTriviaAnswerSifter();

        System.out.println("Sifter Name: " + sifter.getName());
        System.out.println("Sifted Text: " + sifter.sift());
    }
}
