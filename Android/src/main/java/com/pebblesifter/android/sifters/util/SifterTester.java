package com.pebblesifter.android.sifters.util;

import com.pebblesifter.android.sifters.PebbleSifter;
import com.pebblesifter.android.sifters.exampleSifters.*;
import com.pebblesifter.android.sifters.personalSifters.*;

public class SifterTester {
    public static void main(String [] args)
    {
        // Change sifter to the type you would like to test
        PebbleSifter sifter = new TeamTriviaAnswerSifter();

        System.out.println("Sifter Name: " + sifter.getFullName());
        System.out.println("Sifted Text: " + sifter.sift());
    }
}
