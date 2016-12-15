package macrophage.dig.handler;

import macrophage.dig.api.resource.IResource;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DegradationHandler {
    private static class DegradationBlock {
        public BlockPos blockPos;
        public Integer  degradationProgress;
        public boolean  almostFinished;

        public DegradationBlock(BlockPos blockPos, Integer degradationProgress) {
            this.blockPos = blockPos;
            this.degradationProgress = degradationProgress;
            this.almostFinished = false;
        }
    }

    private static Map<DegradationBlock, List<IResource>> mBlocks = new HashMap<DegradationBlock, List<IResource>>();

    public static void addBlock(BlockPos blockPos, Integer maxDegValue, List<IResource> resources) {
        if (blockPos == null) return;
        if (resources == null) return;
        if (maxDegValue == null) return;

        mBlocks.put(new DegradationBlock(blockPos, maxDegValue), resources);
    }

    public static void delBlock(BlockPos blockPos) {
        DegradationBlock blockToDelete = getDegradationBlock(blockPos);
        if (blockToDelete == null) return;
        mBlocks.remove(blockToDelete);
    }

    public static void decrDegradationValue(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return;
        db.degradationProgress--;
        List<IResource> resources = mBlocks.get(getDegradationBlock(blockPos));
        mBlocks.remove(getDegradationBlock(blockPos));
        mBlocks.put(db, resources);
    }

    public static void setAlmostFinished(BlockPos blockPos, boolean value) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return;
        db.almostFinished = value;
        List<IResource> resources = mBlocks.get(getDegradationBlock(blockPos));
        mBlocks.remove(getDegradationBlock(blockPos));
        mBlocks.put(db, resources);
    }

    public static Integer getDegradationValue(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return -1;
        return db.degradationProgress;
    }

    public static Double getDegradationProgress(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return -1D;
        Double progressD = db.degradationProgress.doubleValue() / mBlocks.get(db).get(0).getMaxBlockDegradation().doubleValue();
        return progressD * 100;
    }

    public static List<IResource> getBlockResources(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return null;
        return mBlocks.get(db);
    }

    public static boolean hasDegradation(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        return db != null;
    }

    public static boolean hasAlmostFinished(BlockPos blockPos) {
        DegradationBlock db = getDegradationBlock(blockPos);
        if (db == null) return false;
        return db.almostFinished;
    }

    private static DegradationBlock getDegradationBlock(BlockPos blockPos) {
        for (DegradationBlock db : mBlocks.keySet()) {
            if (db.blockPos.equals(blockPos)) {
                return db;
            }
        }
        return null;
    }
}
