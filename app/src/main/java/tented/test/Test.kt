package tented.test

import com.saki.aidl.PluginMsg
import tented.util.description

/**
 * Test
 * @author Hoshino Tented
 * @date 2018/1/14 16:03
 */

fun test( msg : PluginMsg )
{
    //saki.demo.Demo.debug(msg.description())

    //println(PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_LIST)?.data?.get("troop"))
    //println(PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_MEMBER, group = msg.group)?.data?.get("member"))
}