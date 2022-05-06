package krzyhau.p2movement.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public float stepHeight;

    @Shadow public World world;

    @Shadow public abstract Box getBoundingBox();

    @Shadow protected boolean onGround;

    @Inject(method = "adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("HEAD"), cancellable = true)
    private void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        boolean bl4;
        Box box = this.getBoundingBox();
        List<VoxelShape> list = this.world.getEntityCollisions(((Entity) (Object)this), box.stretch(movement));
        Vec3d vec3d = movement.lengthSquared() == 0.0 ? movement : Entity.adjustMovementForCollisions(((Entity) (Object)this), movement, box, this.world, list);
        boolean bl = movement.x != vec3d.x;
        boolean bl2 = movement.y != vec3d.y;
        boolean bl3 = movement.z != vec3d.z;
        boolean bl5 = bl4 = this.onGround || bl2 && movement.y < 0.0;
        if (this.stepHeight > 0.0f && bl4 && (bl || bl3)) {
            Vec3d vec3d4;
            Vec3d vec3d2 = Entity.adjustMovementForCollisions(((Entity) (Object)this), new Vec3d(movement.x, this.stepHeight, movement.z), box, this.world, list);
            Vec3d vec3d3 = Entity.adjustMovementForCollisions(((Entity) (Object)this), new Vec3d(0.0, this.stepHeight, 0.0), box.stretch(movement.x, 0.0, movement.z), this.world, list);
            if (vec3d3.y < (double)this.stepHeight && (vec3d4 = Entity.adjustMovementForCollisions(((Entity) (Object)this), new Vec3d(movement.x, 0.0, movement.z), box.offset(vec3d3), this.world, list).add(vec3d3)).horizontalLengthSquared() > vec3d2.horizontalLengthSquared()) {
                vec3d2 = vec3d4;
            }
            if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
                cir.setReturnValue(vec3d2.add(Entity.adjustMovementForCollisions(((Entity) (Object)this), new Vec3d(0.0, -vec3d2.y + movement.y, 0.0), box.offset(vec3d2), this.world, list)));
            }
        }
        cir.setReturnValue(vec3d);
    }

}
