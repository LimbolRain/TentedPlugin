package tented.handle

import tented.extra.getPath

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * 一个Handler子类的loader
 */
object PluginLoader
{
    val pluginArray = arrayOf(      //所有的Handler子类的数组
                                tented.handle.plugin.Main,
                                tented.handle.plugin.Master,
                                tented.handle.plugin.Money,
                                tented.handle.plugin.Timer,
                                tented.handle.plugin.Manager,
                                tented.handle.plugin.translate.Translate,
                                tented.handle.plugin.ban.Ban,
                                tented.handle.plugin.photo.Photo,
                                tented.handle.plugin.shop.system.SystemShop,
                                tented.handle.plugin.Settings,
                                tented.handle.plugin.wiki.BaiduWiki,
                                tented.handle.plugin.CodeViewer,
                                tented.handle.plugin.game.SpellCard,
                                tented.handle.plugin.SwitchSystem
                             )

    val pluginList : ArrayList<Handler> = ArrayList(pluginArray.toList())       //实际加载的一个集合, 和数组分开主要是实现开关系统

    operator fun get(clazz : String) : Boolean = tented.file.File.read(tented.file.File.getPath("switch.cfg"), clazz, "true") == "true"
    operator fun set(clazz : String, mod : Boolean) = tented.file.File.write(tented.file.File.getPath("switch.cfg"), clazz, mod.toString())

    //fun pluginCount() : Int = pluginList.size       //获取插件数量, 目前用不到, 也没用...

    fun insertPlugins()             //目前大概也就是一个测试的阶段吧, 还得搞个默认的pluginList...
    {
        val file = java.io.File(tented.file.File.getPath("switch.cfg"))
        val properties = java.util.Properties()

        if( ! file.exists() )
        {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        properties.load(java.io.FileInputStream(file))
        properties.keys.map {
                                if(properties.getProperty(it.toString(), "true") == "false")
                                {
                                    val instance = Class.forName(it.toString()).getField("INSTANCE").get(null) as? Handler

                                    if(instance != null)
                                    {
                                        pluginList.remove(instance)     //remove from plugin list
                                        tented.handle.plugin.Main.menuSet.remove(instance.name)     //remove from menu
                                    }
                                }
                            }
    }

    /**
     * 处理消息函数
     * @param msg 接受处理的PluginMsg对象
     */
    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        if( ! tented.handle.plugin.Settings.doInit(msg) || tented.handle.plugin.ban.Ban.checkBan(msg) ) return

        for ( handler in pluginList)        //迭代遍历所有的Handler子类
        {
            msg.clearMsg()          //清楚消息
            handler.handle(msg)     //执行处理
            msg.send()              //发送消息, 所以只要在处理里面添加消息就好了
        }
    }
}