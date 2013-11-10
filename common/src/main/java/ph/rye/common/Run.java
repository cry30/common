package ph.rye.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ph.rye.common.io.ReadWriteTextFileJDK7;

public class Run {


    public static void main(final String[] args) throws IOException
    {
        final List<String> list = new ReadWriteTextFileJDK7()
                .readSmallTextFile("d:\\File1.txt");


        final List<String> install = new ArrayList<String>();
        final List<String> pom = new ArrayList<String>();
        for (final String string : list) {

            final String artifact = string.substring(
                    string.lastIndexOf('/') + 1, string.indexOf('.'));

            install.add("call mvn install:install-file -DgroupId=oaf-jdbc "
                    + "-DartifactId=" + artifact
                    + " -Dversion=1 -Dpackaging=jar -Dfile=" + string);

            pom.add(" <dependency>");
            pom.add("\t<groupId>oaf-jdbc</groupId>");
            pom.add("\t<artifactId>" + artifact + "</artifactId>");
            pom.add("\t<version>1</version>");
            pom.add("</dependency>");
        }

        for (final String string : install) {
            System.out.println(string);

        }

        System.out.println();


        for (final String string : pom) {
            System.out.println(string);


        }


    }

}
