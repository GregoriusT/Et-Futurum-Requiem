package ganymedes01.etfuturum.world.generate.feature;

import java.util.Random;

import ganymedes01.etfuturum.configuration.configs.ConfigWorld;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenFossil extends WorldGenerator {
	
	public static Block bone = ConfigWorld.fossilBoneBlock;
	
	public WorldGenFossil() {
	}

	public boolean generateSpecificFossil(World world, Random random, int x, int y, int z, int rotation, int type, boolean hasCoal) {
		Fossil fossil = getFossilFromType(type);
		if(fossil != null && canFossilGenerateHere(world, x, y, z, fossil)) {
			fossil.build(world, random, x, y, z, type, rotation, hasCoal);
			return true;
		}
		return false;
	}
	
	private boolean canFossilGenerateHere(World world, int x, int y, int z, Fossil fossil) {
		int air = 0;
		if(isInvalidCorner(world, x, y, z)) {
			air++;
		}
		if(isInvalidCorner(world, x + fossil.getCorners()[0], y, z)) {
			air++;
		}
		if(isInvalidCorner(world, x, y, z + fossil.getCorners()[2])) {
			air++;
		}
		if(isInvalidCorner(world, x + fossil.getCorners()[0], y, z + fossil.getCorners()[2])) {
			air++;
		}
		if(isInvalidCorner(world, x, y + fossil.getCorners()[1], z)) {
			if(air++ >= 5) return false;
		}
		if(isInvalidCorner(world, x + fossil.getCorners()[0], y + fossil.getCorners()[1], z)) {
			if(air++ >= 5) return false;
		}
		if(isInvalidCorner(world, x, y + fossil.getCorners()[1], z + fossil.getCorners()[2])) {
			if(air++ >= 5) return false;
		}
		if(isInvalidCorner(world, x + fossil.getCorners()[0], y + fossil.getCorners()[1], z + fossil.getCorners()[2])) {
			if(air++ >= 5) return false;
		}
		return air < 5;
	}
	
	private boolean isInvalidCorner(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return world.canBlockSeeTheSky(x, y, z) && block.isAir(world, x, y, z) && !block.isOpaqueCube();
	}
	
	@Override
	public boolean generate(World world, Random random, int x, int y, int z) {
		return this.generateSpecificFossil(world, random, x, y, z, /*random.nextInt(5)*/0, random.nextInt(8), random.nextInt(2) == 1);
	}

	public Fossil getFossilFromType(int i) {
		switch(i) {
		case 0: return new Fossil_Skull_1();
		case 1: return new Fossil_Skull_2();
		case 2: return new Fossil_Skull_3();
		case 3: return new Fossil_Skull_4();
		case 4: return new Fossil_Spine_1();
		case 5: return new Fossil_Spine_2();
		case 6: return new Fossil_Spine_3();
		case 7: return new Fossil_Spine_4();
		default: return null;
		}
	}
	
public static abstract class Fossil {
	
	protected int[] corners;
	
	public Fossil() {
	}
	
	public void fillBlocks(World world, Block block, int x, int y, int z, int xfrom, int yfrom, int zfrom, int xto, int yto, int zto, int meta, int flag, boolean hasCoal, Random rand, int facing) {
		int swap;

		int xadd1 = xfrom;
		int xadd2 = xfrom + xto;

		int zadd1 = zfrom;
		int zadd2 = zfrom + zto;

		if(facing == 2) {
			xadd1 = xfrom + xto;
			xadd2 = xfrom;

			zadd1 = zfrom + zto;
			zadd2 = zfrom;
		}
		if(facing == 1 || facing == 3) {
			swap = xadd1;
			xadd1 = zadd1;
			zadd1 = swap;

			swap = xadd2;
			xadd2 = zadd2;
			zadd2 = swap;
		}
		int i = x + xadd1;
		while(i < x + xadd2) {
			int j = y + yfrom;
			while(j < y + yfrom + yto) {
				int k = z + zadd1;
				while(k < z + zadd2) {
					if(world.getBlock(i, j, k) == null || world.getBlock(i, j, k).getBlockHardness(world, i, j, k) > -1) {
						if(rand.nextFloat() < 0.9) {
							if(hasCoal && rand.nextFloat() > 0.9) {
								world.setBlock(i, j, k, Blocks.coal_ore, 0, flag);
							} else {
								world.setBlock(i, j, k, block, meta, flag);
							}
						}
					}
					k++;
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * Three ints for the corners of the fossil. First is max X, second is max Y, third is max Z
	 */
	public abstract int[] getCorners();
	
	public abstract void build(World world, Random rand, int x, int y, int z, int type, int rotation, boolean hasCoal);
	
//  public abstract int[] getCorners();
}

public class Fossil_Skull_1 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 4, 1, 3, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 0, 4, 1, 1, 2, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 4, 1, 1, 2, rotation, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 0, 1, 2, 1, 3, 3, rotation2, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 1, 2, 1, 3, 3, rotation2, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 1, 0, 1, 1, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 0, 1, 1, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 2, 0, 4, 1, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 3, 0, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 4, 1, 4, 1, 5, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 1, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 1, 6, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 1, 6, 2, 3, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 6, 1, 3, 1, 0, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {5, 4, 6};
		}
		return corners;
	}
}

public class Fossil_Skull_2 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 5, 1, 3, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 1, 4, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 1, 4, 3, 3, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 1, 4, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 6, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 1, 2, 1, 3, 1, rotation2, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 1, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 2, 1, 3, 1, rotation2, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 1, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 2, 0, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 0, 1, 1, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 4, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 4, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 1, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 1, 4, 1, 5, 1, 3, rotation, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {6, 4, 4};
		}
		return corners;
	}
}

public class Fossil_Skull_3 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 3, 1, 4, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 0, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 1, 0, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 2, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 0, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 1, 4, 3, 2, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 4, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 2, 3, rotation2, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 4, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 1, 1, 2, 3, rotation2, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 4, 3, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 0, 3, 1, 4, rotation, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {4, 3, 4};
		}
		return corners;
	}
}

public class Fossil_Skull_4 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		//int rotation2 = (facing % 2) == 1 ? 4 : 8; // unused variable
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 2, 1, 2, rotation, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 0, 0, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 1, 0, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 0, 1, 3, 1, 0, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 3, 1, 1, 1, 2, 2, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 2, 2, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 1, 3, 2, 2, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 3, 0, 2, 1, 3, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 0, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {3, 3, 3};
		}
		return corners;
	}
}

public class Fossil_Spine_1 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		//int rotation = (facing % 2) == 0 ? 4 : 8; // unused variable
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		
		fillBlocks(world, bone, x, y, z, 0, 0, 1, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 3, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 5, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 7, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 9, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 11, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 2, 0, 1, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 0, 3, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 0, 5, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 0, 7, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 0, 9, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 2, 0, 11, 1, 2, 1, 0, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 1, 2, 0, 1, 1, 13, rotation2, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {2, 2, 12};
		}
		return corners;
	}
}

public class Fossil_Spine_2 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 3, 0, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 0, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 3, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 3, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 3, 3, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 3, 3, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 3, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 5, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 7, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 9, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 11, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 4, 1, 1, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 3, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 5, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 7, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 9, 1, 2, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 1, 11, 1, 2, 1, 0, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 2, 3, 0, 1, 1, 13, rotation2, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {4, 3, 12};
		}
		return corners;
	}
}

public class Fossil_Spine_3 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		
		fillBlocks(world, bone, x, y, z, 0, 0, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 7, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 9, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 0, 11, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 6, 0, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 7, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 9, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 11, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 5, 0, 1, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 0, 3, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 0, 5, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 0, 7, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 0, 9, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 0, 11, 1, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 3, 1, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 3, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 5, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 7, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 9, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 3, 11, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 4, 3, 1, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 3, 3, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 3, 5, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 3, 7, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 3, 9, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 4, 3, 11, 3, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 3, 3, 0, 1, 1, 13, rotation2, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {6, 3, 12};
		}
		return corners;
	}
}

public class Fossil_Spine_4 extends Fossil {

	@Override
	public void build(World world, Random rand, int x, int y, int z, int type, int facing, boolean hasCoal) {
		int rotation = (facing % 2) == 0 ? 4 : 8;
		int rotation2 = (facing % 2) == 1 ? 4 : 8;
		
		fillBlocks(world, bone, x, y, z, 1, 0, 1, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 3, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 5, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 7, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 9, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 1, 0, 11, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 6, 0, 1, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 3, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 5, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 7, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 9, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 6, 0, 11, 2, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 0, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 7, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 9, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 1, 11, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 8, 1, 1, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 8, 1, 3, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 8, 1, 5, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 8, 1, 7, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 8, 1, 9, 1, 3, 1, 0, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 8, 1, 11, 1, 3, 1, 0, 3, hasCoal, rand, facing);

		fillBlocks(world, bone, x, y, z, 0, 4, 1, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 4, 3, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 4, 5, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 4, 7, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 4, 9, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 0, 4, 11, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 5, 4, 1, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 4, 3, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 4, 5, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 4, 7, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 4, 9, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		fillBlocks(world, bone, x, y, z, 5, 4, 11, 4, 1, 1, rotation, 3, hasCoal, rand, facing);
		
		fillBlocks(world, bone, x, y, z, 4, 4, 0, 1, 1, 13, rotation2, 3, hasCoal, rand, facing);
	}

	@Override
	public int[] getCorners() {
		if(corners == null) {
			corners = new int[] {8, 4, 12};
		}
		return corners;
	}
}
}
