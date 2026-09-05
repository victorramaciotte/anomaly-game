package level;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.Block;
import entity.BlockState;
import entity.BlockType;
import main.GameConfig;
import main.ImageLoader;

public class LevelBuilder {
    static int tile = GameConfig.TILE_SIZE;

    public static LevelData build(StageConfig config) {
        Double coreX = null, coreY = null;
        List<Block> blocks = new ArrayList<>();
        List<String> layout = config.getLayout();
        int levelWidth = layout.get(0).length() * tile;
        int levelHeight = layout.size() * tile;

        for (int row = 0; row < layout.size(); row++) {
            String line = layout.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);

                double x = col * tile;
                double y = row * tile;

                if (c == 'C') {
                    if (coreX != null) {
                        throw new IllegalStateException("Múltiplos núcleos no layout: " + config.getIndex());
                    }
                    coreX = x;
                    coreY = y;
                    continue; // não vira Block
                }

                Block block = parseChar(c, x, y, config.getIndex());
                if (block != null) blocks.add(block);
            }
        }
        if (coreX == null) {
            throw new IllegalStateException("Layout sem núcleo definido: " + config.getIndex());
        }

        return new LevelData(blocks, coreX, coreY, levelHeight, levelWidth);
    }

    private static Block parseChar(char c, double x, double y, int stageIndex) {
        return switch (c) {
            case '#' -> new Block(x, y, BlockType.INVISIBLE, BlockState.NORMAL, stageIndex);
            case 'F' -> new Block(x, y, BlockType.FILL, BlockState.NORMAL, stageIndex);
            case 'N' -> new Block(x, y, BlockType.NORMAL,  BlockState.NORMAL,stageIndex);
            case 'n' -> new Block(x, y, BlockType.NORMAL,  BlockState.NORMAL,stageIndex, 1);
            case 'M' -> new Block(x, y, BlockType.NORMAL,  BlockState.NORMAL,stageIndex, 2);
            case 'B' -> new Block(x, y, BlockType.BREAKABLE, BlockState.NORMAL, stageIndex);
            case 'b' -> new Block(x, y, BlockType.BREAKABLE, BlockState.CRACKED, stageIndex);
            case '.' -> null;
            default -> throw new IllegalStateException("Caractere desconhecido: '" + c + "'");
        };
    }
    
    public static BufferedImage resolveSprite(int stageIndex, BlockType type, BlockState state, int variant) {
        String key = spriteKeyFor(type, state, variant);
        if (key == null) return null;
        return ImageLoader.load("resources/images/blocks/stage" + stageIndex + "/" + key + ".png");
    }

    private static String spriteKeyFor(BlockType type, BlockState state, int variant) {
    	String suffix = (variant > 0) ? "_v" + variant : "";
    	
    	if (state == BlockState.CORRUPTED) {
    		String base = switch (type) {
            case BREAKABLE -> "breakable";
            case FILL -> "fill";
            default -> "normal";
        };
        return base + "_corrupted" + suffix;
        }
        if (type == BlockType.BREAKABLE && state == BlockState.CRACKED) {
            return "breakable_cracked" + suffix;
        }
        return switch (type) {
            case NORMAL -> "normal" + suffix;
            case BREAKABLE -> "breakable" + suffix;
            case FILL -> "fill" + suffix;
            default -> null;
        };
    }
}