package level;

import java.util.List;

public class Campaign {
    private List<StageConfig> stages;
    private int currentIndex = 0;

    public Campaign(List<StageConfig> stages) {
        this.stages = stages;
    }

    public StageConfig getCurrentStage() {
        return stages.get(currentIndex);
    }
    
    public int getTotalStages() {
    	return stages.size();
    }
    
    public int getCurrentStageNumber() {
    	return currentIndex + 1;
    }

    public boolean hasNextStage() {
        return currentIndex + 1 < stages.size();
    }

    public void advance() {
        if (hasNextStage()) currentIndex++;
    }
}