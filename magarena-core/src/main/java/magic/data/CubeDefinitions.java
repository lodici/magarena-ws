package magic.data;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import magic.utility.MagicSystem;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class CubeDefinitions {

    private static final String[] INCLUDED_CUBES={"all"};
    private static final String CUBE_FILE_EXTENSION="_cube.txt";

    static final String DEFAULT_NAME=INCLUDED_CUBES[0];

    private static final FileFilter CUBE_FILE_FILTER=new FileFilter() {
        @Override
        public boolean accept(final File file) {
            return file.isFile()&&file.getName().endsWith(CUBE_FILE_EXTENSION);
        }
    };

    private static final List<MagicCubeDefinition> cubeDefinitions;

    static {
        cubeDefinitions=new ArrayList<>();
        for (final String cubeName : INCLUDED_CUBES) {
            cubeDefinitions.add(new MagicCubeDefinition(cubeName));
        }
    }

    public static String[] getCubeNames() {
        final String[] names=new String[cubeDefinitions.size()];
        for (int index=0;index<names.length;index++) {
            names[index]=cubeDefinitions.get(index).getName();
        }
        return names;
    }

    public static String[] getFilterValues() {
        final List<String> values = new ArrayList<>();
        for (MagicCubeDefinition cube : cubeDefinitions) {
            if (!cube.toString().equalsIgnoreCase("all")) {
              values.add(cube.toString());
          }
        }
        return values.toArray(new String[values.size()]);
    }

    public static MagicCubeDefinition getCubeDefinition(final String name) {
        for (final MagicCubeDefinition cubeDefinition : cubeDefinitions) {
            if (cubeDefinition.getName().equals(name)) {
                return cubeDefinition;
            }
        }
        return cubeDefinitions.get(0);
    }

    private static void loadCubeDefinition(final String name,final File file) {
        List<String> content = Collections.emptyList();
        try { //load cube
            content = FileIO.toStrList(file);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + name);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return;
        }
        final MagicCubeDefinition cubeDefinition = new MagicCubeDefinition(name);
        for (final String line: content) {
            final String cardName = line.trim();
            if (!cardName.isEmpty()) {
                cubeDefinition.add(cardName);
            }
        }
        cubeDefinitions.add(cubeDefinition);
    }

    public static void loadCubeDefinitions() {
        final File[] cubeFiles = MagicFileSystem.getDataPath(DataPath.MODS).toFile().listFiles(CUBE_FILE_FILTER);
        if (cubeFiles!=null) {
            for (final File file : cubeFiles) {
                final String name = file.getName();
                final int index = name.indexOf(CUBE_FILE_EXTENSION);
                loadCubeDefinition(name.substring(0,index),file);
            }
        }

        if (MagicSystem.showStartupStats()) {
            System.err.println(cubeDefinitions.size()+" cube definitions");
            for (final MagicCubeDefinition cubeDefinition : cubeDefinitions) {
                System.err.println("Cube "+cubeDefinition.getName());
            }
        }
    }

    public static boolean isCardInCube(MagicCardDefinition card, String cubeName) {
        final MagicCubeDefinition cube = getCube(cubeName);
        return cube.contains(card.getName());
    }

    //TODO: convert cubeDefinitions to a Map keyed on cubeName then can get rid of this function.
    private static MagicCubeDefinition currentCube = null;
    public static MagicCubeDefinition getCube(final String cubeName) {
        if (currentCube == null || !currentCube.toString().equals(cubeName)) {
            for (MagicCubeDefinition cube : cubeDefinitions) {
                if (cube.toString().equals(cubeName)) {
                    currentCube = cube;
                    break;
                }
            }
        }
        return currentCube;
    }

    public static MagicCubeDefinition createCube(Collection<MagicCardDefinition> cardPool) {
        final MagicCubeDefinition cubeDefinition = new MagicCubeDefinition("random");
        for (MagicCardDefinition card : cardPool) {
            cubeDefinition.add(card.getName());
        }
        return cubeDefinition;
    }
}
