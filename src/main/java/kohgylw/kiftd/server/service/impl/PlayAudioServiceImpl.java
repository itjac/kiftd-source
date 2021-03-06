package kohgylw.kiftd.server.service.impl;

import kohgylw.kiftd.server.service.*;
import org.springframework.stereotype.*;

import com.google.gson.Gson;

import kohgylw.kiftd.server.mapper.*;
import javax.annotation.*;
import javax.servlet.http.*;
import kohgylw.kiftd.server.pojo.*;
import kohgylw.kiftd.server.enumeration.*;
import java.util.*;
import kohgylw.kiftd.server.model.*;
import kohgylw.kiftd.server.util.*;

@Service
public class PlayAudioServiceImpl implements PlayAudioService
{
    @Resource
    private NodeMapper fm;
    @Resource
    private AudioInfoUtil aiu;
    @Resource
    private Gson gson;
    
    private AudioInfoList foundAudios(final HttpServletRequest request) {
        final String fileId = request.getParameter("fileId");
        if (fileId != null && fileId.length() > 0) {
            final String account = (String)request.getSession().getAttribute("ACCOUNT");
            if (ConfigureReader.instance().authorized(account, AccountAuth.DOWNLOAD_FILES)) {
                final List<Node> blocks = (List<Node>)this.fm.queryBySomeFolder(fileId);
                return this.aiu.transformToAudioInfoList(blocks, fileId);
            }
        }
        return null;
    }
    
    /**
     * <h2>解析播放音频文件</h2>
     * <p>根据音频文件的ID查询音频文件节点，以及同级目录下所有音频文件组成播放列表，并返回节点JSON信息，以便发起播放请求。</p>
     * <pre>
     * “月隐山河，风敲驼铃，和调成歌远去堪为异乡客……”
     * </pre>
     * @author kohgylw
     * @param request javax.servlet.http.HttpServletRequest 请求对象
     * @return String 视频节点的JSON字符串
     */
    public String getAudioInfoListByJson(final HttpServletRequest request) {
        final AudioInfoList ail = this.foundAudios(request);
        if (ail != null) {
            return gson.toJson((Object)ail);
        }
        return "ERROR";
    }
}
