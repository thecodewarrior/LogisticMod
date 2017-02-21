package thecodewarrior.logistic.logistics

import com.teamwizardry.librarianlib.common.network.PacketBase
import com.teamwizardry.librarianlib.common.util.autoregister.PacketRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side
import java.util.*

/**
 * Created by TheCodeWarrior
 */
@PacketRegister(Side.CLIENT)
class PacketAddNodes : PacketBase() {
    @Save var data: Array<BareLogisticNode> = arrayOf()
    @Save var ids: Array<UUID> = arrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in ids.indices) {
            ClientLogisticTracker.nodes[ids[i]] = data[i]
        }
    }
}
@PacketRegister(Side.CLIENT)
class PacketRemoveNodes : PacketBase() {
    @Save var ids: Array<UUID> = arrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in ids.indices) {
            ClientLogisticTracker.nodes.remove(ids[i])
            ClientLogisticTracker.edges.removeAll { it.aID == ids[i] || it.bID == ids[i] }
        }
    }
}
@PacketRegister(Side.CLIENT)
class PacketAddEdges : PacketBase() {
    @Save var idsFrom: Array<UUID> = arrayOf()
    @Save var idsTo: Array<UUID> = arrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in idsFrom.indices) {
            val e = EdgeData(idsFrom[i], idsTo[i])
            ClientLogisticTracker.updateEdgeEndpoints(e)
            ClientLogisticTracker.edges.add(e)
        }
    }
}

@PacketRegister(Side.CLIENT)
class PacketRemoveEdges : PacketBase() {
    @Save var idsFrom: Array<UUID> = arrayOf()
    @Save var idsTo: Array<UUID> = arrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in idsFrom.indices) {
            val e = EdgeData(idsFrom[i], idsTo[i])
            ClientLogisticTracker.edges.remove(e)
        }
    }
}

@PacketRegister(Side.CLIENT)
class PacketAddDrones : PacketBase() {
    @Save var ids: IntArray = intArrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in ids.indices) {
            ClientLogisticTracker.createDrone(ids[i])
        }
    }
}

@PacketRegister(Side.CLIENT)
class PacketSetPaths: PacketBase() {
    @Save var ids: IntArray = intArrayOf()
    @Save var paths: Array<Array<UUID>> = arrayOf()

    override fun handle(ctx: MessageContext) {
        for(i in ids.indices) {
            ClientLogisticTracker.setDronePath(ids[i], paths[i])
        }
    }
}