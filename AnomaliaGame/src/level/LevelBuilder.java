package level;

import java.util.ArrayList;
import java.util.List;

import entity.Block;
import entity.BlockState;
import entity.BlockType;
import main.GameConfig;

public class LevelBuilder {
    static int tile = GameConfig.TILE_SIZE;

    public static LevelData build(StageConfig config) {
        Double coreX = null, coreY = null;
        List<Block> blocks = new ArrayList<>();
        List<String> layout = config.getLayout();

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

                Block block = parseChar(c, x, y);
                if (block != null) blocks.add(block);
            }
        }
        if (coreX == null) {
            throw new IllegalStateException("Layout sem núcleo definido: " + config.getIndex());
        }

        return new LevelData(blocks, coreX, coreY);
    }

    private static Block parseChar(char c, double x, double y) {
        switch (c) {
            case '#': return new Block(x, y, BlockType.FILL, BlockState.NORMAL);
            case 'N': return new Block(x, y, BlockType.NORMAL, BlockState.NORMAL);
            case 'B': return new Block(x, y, BlockType.BREAKABLE, BlockState.NORMAL);
            case 'b': return new Block(x, y, BlockType.BREAKABLE, BlockState.CRACKED);
            case '.': return null; // vazio
            default:
                throw new IllegalStateException("Caractere desconhecido no layout: '" + c + "'");
        }
    }
}