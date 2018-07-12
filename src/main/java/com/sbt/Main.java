package com.sbt;

import com.sbt.controllers.VKRequestController;
import com.sbt.views.VKView;

public class Main {
    public static void main(String[] args) {
        new VKView("Druzhestvennyi interface 3.", 700, 600, new VKRequestController());
    }
}
