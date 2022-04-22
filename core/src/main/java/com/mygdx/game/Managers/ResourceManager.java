package com.mygdx.game.Managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

/**
 * Manages all assets and disposes of them when appropriate
 */
public final class ResourceManager {
    private static boolean initialized = false;
    private static boolean loaded;
    private static AssetManager manager;
    private static ArrayList<String> ids;
    private static ArrayList<TiledMap> tileMaps;
    //private static HashMap<String, FreeTypeFontGenerator> fontGenerators;
    //private static HashMap<String, BitmapFont> fonts;

    /**
     * The equivalent to a constructor
     */
    public static void Initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        manager = new AssetManager();
        loaded = false;
        ids = new ArrayList<>();
        tileMaps = new ArrayList<>();
        //fontGenerators = new HashMap<>();
        //fonts = new HashMap<>();
    }

    /**
     * Schedules an asset for loading
     * Amended for assessment 2: Made the method return void
     * @param fPath the file path of the asset
     */
    public static void addTexture(String fPath) {
        tryInit();
        checkAdd();
        manager.load(fPath, Texture.class);
        ids.add(fPath);
    }

    /**
     * Schedules an asset for loading
     * Amended for assessment 2: Made the method return void
     * @param fPath the file path of the asset
     */
    public static void addTextureAtlas(String fPath) {
        tryInit();
        checkAdd();
        manager.load(fPath, TextureAtlas.class);
        ids.add(fPath);
    }

    /**
     * Prefaces name with |TM| followed by the internal index of the tilemap however this isn't required to access this asset
     * @param fPath the file location of the asset
     * @return id of the asset
     */
    public static int addTileMap(String fPath) {
        tryInit();
        checkAdd();
        TiledMap map = new TmxMapLoader().load(fPath);
        tileMaps.add(map);
        ids.add("|TM|" + tileMaps.size() + fPath);
        return ids.size();
    }

    /**
     * Actually loads the assets
     */
    public static void loadAssets() {
        tryInit();
        loaded = true;
        manager.finishLoading();
    }

    public static Texture getTexture(String fPath) {
        tryInit();
        return manager.get(fPath);
    }

    public static TextureAtlas getTextureAtlas(String fPath) {
        tryInit();
        TextureAtlas t = manager.get(fPath);
        for (Texture t0 : t.getTextures()) {
            t0.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        return manager.get(fPath);
    }

    public static Texture getTexture(int id) {
        tryInit();
        String fPath = ids.get(id - 1);
        return manager.get(fPath);
    }

    public static TextureAtlas getTextureAtlas(int id) {
        tryInit();
        String fPath = ids.get(id - 1);
        return getTextureAtlas(fPath);
    }

    /**
     * @param atlas_id the id of the source texture atlas
     * @param name     the name of the texture
     * @return the found Sprite in the given atlas
     */
    public static Sprite getSprite(int atlas_id, String name) {
        // Sprite s =  getTextureAtlas(atlas_id).createSprite(name);
        // s.setU(0);
        // s.setV(0);
        // s.setU2(0.125f);
        // s.setV2(0.25f);
        //TextureAtlas t = getTextureAtlas(atlas_id);
        return getTextureAtlas(atlas_id).createSprite(name);
    }

    /**
     * Gets the tile map returns null if not a tile map
     *
     * @param id the id of the tile map
     * @return the tile map
     */
    public static TiledMap getTileMap(int id) {
        tryInit();

        String fPath = ids.get(id - 1);
        if (fPath.length() < 4) {
            return null;
        }
        String slice = fPath.substring(0, 4);
        if (!slice.equals("|TM|")) {
            return null;
        }
        int id_ = Character.getNumericValue(fPath.charAt(4));
        return tileMaps.get(id_ - 1);
    }

    /**
     * only looks for simple assets not specialty ones so largely only textures
     *
     * @param name the desired asset name
     * @return the id of the asset found if found
     */
    public static int getId(String name) {
        //tryInit();
        return ids.indexOf(name) + 1;
    }

    /**
     * It is imperative that this is called unless you want memory leeks
     */
    public static void cleanUp() {
        tryInit();
        manager.dispose();
        for (TiledMap map : tileMaps) {
            map.dispose();
        }
        //for (BitmapFont font : fonts.values()) {
        //    font.dispose();
        //}
        //for (FreeTypeFontGenerator generator : fontGenerators.values()) {
        //    generator.dispose();
        //}
    }

    /**
     * Will check if new assets can be added if not throw an error
     */
    private static void checkAdd() {
        if (loaded) {
            throw new RuntimeException("Can't load assets at this stage");
        }
    }

    /**
     * Calls Initialize if not already done so
     */
    private static void tryInit() {
        if (!initialized) {
            Initialize();
        }
    }

    /**
     * Added for Assessment 2
     * Calls cleanup function and disposes of remaining assets
     */
    public static void dispose() {
        cleanUp();
        initialized = false;
        loaded = false;
        ids = new ArrayList<>();
        //fonts = new HashMap<>();
    }

    //    public static int addFontGenerator(String fontPath) {
    //        tryInit();
    //        checkAdd();
    //        if (fontGenerators.containsKey(fontPath)) {
    //            return -1;
    //        }
    //        fontGenerators.put(fontPath, new FreeTypeFontGenerator(Gdx.files.internal(fontPath)));
    //        ids.add("|FG|" + fontPath);
    //
    //        return ids.size();
    //    }
    //
    //    public static int createFont(int font_generator_id, int fontSize) {
    //        tryInit();
    //        String fontName = ids.get(font_generator_id - 1);
    //        fontName = fontName.substring(4);
    //        FreeTypeFontGenerator generator = fontGenerators.get(fontName);
    //
    //        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
    //        params.size = fontSize;
    //        params.color.r = 0;
    //        params.color.g = 0;
    //        params.color.b = 0;
    //
    //        BitmapFont font = generator.generateFont(params);
    //
    //        ids.add("|FT|" + fontSize + fontName);
    //        fonts.put(fontSize + fontName, font);
    //
    //        return ids.size();
    //    }
    //
    //    public static int createFont(String fontName, int fontSize) {
    //        tryInit();
    //
    //        if (!fontGenerators.containsKey(fontName)) {
    //            return -1;
    //        }
    //        FreeTypeFontGenerator generator = fontGenerators.get(fontName);
    //
    //        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
    //        params.size = fontSize;
    //        params.color.r = 1;
    //        params.color.g = 1;
    //        params.color.b = 1;
    //
    //        BitmapFont font = generator.generateFont(params);
    //
    //        ids.add("|FT|" + fontSize + fontName);
    //        fonts.put(fontSize + fontName, font);
    //
    //        return ids.size();
    //    }
    //
    //    public static TiledMap getTileMap(String fPath) {
    //        tryInit();
    //        int id = -1;
    //        for (String fName : ids) {
    //            if (fName.length() < 4) {
    //                continue;
    //            }
    //            String slice = fName.substring(0, 4);
    //            if (slice.equals("|TM|") && fName.contains(fPath)) {
    //                id = Character.getNumericValue(fName.charAt(4));
    //                break;
    //            }
    //        }
    //        if (id < 0) {
    //            return null;
    //        }
    //        return tileMaps.get(id - 1);
    //    }
    //
    //    public static BitmapFont getFont(int font_id) {
    //        String fontName = ids.get(font_id - 1);
    //        fontName = fontName.substring(4);
    //
    //        return fonts.get(fontName);
    //    }
    //
    //    public static BitmapFont getFont(String fontName) {
    //        return fonts.get(fontName);
    //    }
}
