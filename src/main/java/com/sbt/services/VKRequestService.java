package com.sbt.services;

import com.sbt.models.ApplicationVk;
import com.vk.api.sdk.client.Lang;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.Career;
import com.vk.api.sdk.objects.users.School;
import com.vk.api.sdk.objects.users.University;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.queries.likes.LikesType;
import com.vk.api.sdk.queries.wall.WallGetFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.vk.api.sdk.queries.users.UserField.*;

/**
 * https://oauth.vk.com/authorize?client_id=6623093&display=page&redirect_uri=https://oauth.vk.com/getItForMe.html&scope=offline&response_type=code&v=5.80
 */

public class VKRequestService implements IBaseService {
    private UserActor sActor;
    private VkApiClient vkClient;
    private TransportClient transportClient;

    public VKRequestService(){
            transportClient = HttpTransportClient.getInstance();
            vkClient = new VkApiClient(transportClient);
    }

    public boolean auth(String codeForAuth) throws ClientException, ApiException {
            UserAuthResponse authResponse =  vkClient.oauth()
                    .userAuthorizationCodeFlow(ApplicationVk.APP_ID,
                            ApplicationVk.APP_CLIENT_SECRET,
                            ApplicationVk.RE_DIRECT,codeForAuth)
                    .execute();

            sActor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
            return true;
    }

    @Override
    public String getInfoUserById(String id){
        List<UserXtrCounters> users = null;
        try {
            users = vkClient.users()
                    .get(sActor)
                    .userIds(id)
                    .fields(CITY, STATUS, SEX, BDATE, HOME_TOWN, UNIVERSITIES, SCHOOLS, RELATION,
                            ACTIVITIES, INTERESTS, MUSIC, MOVIES, BOOKS, GAMES, ABOUT, CAREER)
                    .lang(Lang.RU)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getInfoToString(users);
    }

    @Override
    public String getIdByMaxLikesOnWall(String id) throws ClientException, ApiException {
        StringBuilder res = new StringBuilder();
                List<WallPostFull> regs = vkClient.wall()
                        .get(sActor)
                        .ownerId(Integer.parseInt(id))
                        .lang(Lang.RU)
                        .filter(WallGetFilter.OWNER)  //only owner of wall
                        .execute()
                        .getItems();
        ArrayList<Integer> userIds = getIds(regs,id);
        HashMap<Integer,Integer> cntUserIds = getcntUserIds(userIds);

        int maxVal = Collections.max(cntUserIds.values(), Integer::compare);
        res.append("Max Likes on the wall: ").append(maxVal).append("\n");
        for (HashMap.Entry<Integer,Integer> pair: cntUserIds.entrySet()){
            if (pair.getValue()==maxVal) {
                res.append("https://vk.com/id").append(pair.getKey()).append("\n");
            }
        }
        return res.toString();
    }

    private HashMap<Integer,Integer> getcntUserIds(ArrayList<Integer> userIds){
        HashMap<Integer,Integer> cntUserIds = new HashMap<>();
        int cnt;
        for (int i = 0; i < userIds.size(); i++) {
            Integer idUser = userIds.get(i);
            if (!cntUserIds.containsKey(idUser)) {
                cnt = 0;
                for (int j = i; j < userIds.size(); j++) {
                    cnt += idUser.equals(userIds.get(j)) ? 1 : 0;
                }
                cntUserIds.put(idUser, cnt);
            }
        }
        return cntUserIds;
    }

    private ArrayList<Integer> getIds(List<WallPostFull> regs,String id) throws ClientException, ApiException {
        ArrayList<Integer> userIds = new ArrayList<>();
        for (int i = 0; i < regs.size(); i++) {
            userIds.addAll(vkClient.likes()
                    .getList(sActor,LikesType.POST)
                    .ownerId(Integer.parseInt(id))
                    .itemId(regs.get(i).getId())
                    .count(1000)
                    .execute()
                    .getItems());
            if (i%6==0) {
                try {
                    Thread.sleep(2400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return userIds;
    }

    private String getInfoToString(List<UserXtrCounters> list){
        StringBuilder sb = new StringBuilder();
        if (list!=null) {
            UserXtrCounters tmp;
            for (int i = 0; i < list.size(); i++) {
                tmp = list.get(i);
                sb.append("ID - ").append(tmp.getId())
                        .append("\nФамилия - ").append(tmp.getLastName())
                        .append("\nИмя - ").append(tmp.getFirstName())
                        .append("\nСтатус - ").append(tmp.getStatus())
                        .append("\nПол - ").append(tmp.getSex())
                        .append("\nДата рождения - ").append(tmp.getBdate())
                        .append("\nРодной город - ").append(tmp.getHomeTown())
                        .append("\nОтношения - ").append(tmp.getRelation())
                        .append("\nЗанятия - ").append(tmp.getActivities())
                        .append("\nИнтересы - ").append(tmp.getInterests())
                        .append("\nМузыка - ").append(tmp.getMusic())
                        .append("\nФильмы - ").append(tmp.getMovies())
                        .append("\nИгры - ").append(tmp.getGames())
                        .append("\nО себе - ").append(tmp.getAbout())
                        .append("\nОбразование - ");
                appendUniversitySB(sb, tmp.getUniversities());
                sb.append("\nШкола - ");
                appendSchoolsSB(sb, tmp.getSchools());
                sb.append("\nКарьера - ");
                appendJobsSB(sb, tmp.getCareer());
                if (tmp.getCity() != null) {
                    sb.append("\nГород - ").append(tmp.getCity().getTitle());
                }
            }
        }
        return sb.toString();
    }

    private void appendUniversitySB(StringBuilder sb, List<University> list){
        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {
                University tmp = list.get(i);
                sb.append("\nНазвание университета: ").append(tmp.getName())
                        .append("\nКафедра: ").append(tmp.getChairName())
                        .append("\nФорма обучения: ").append(tmp.getEducationForm())
                        .append("\nСтатус обучения: ").append(tmp.getEducationStatus());
            }
        }
    }

    private void appendSchoolsSB(StringBuilder sb, List<School> list){
        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {
                School tmp = list.get(i);
                sb.append("\nНазвание школы: ").append(tmp.getName())
                        .append("\nГода обучения ").append(tmp.getYearFrom())
                        .append(" - ").append(tmp.getYearTo())
                        .append("\nКласс: ").append(tmp.getClassName())
                        .append("\nТип заведения: ").append(tmp.getTypeStr());
            }
        }
    }

    private void appendJobsSB(StringBuilder sb, List<Career> list){
        if (list!=null) {
            for (int i = 0; i < list.size(); i++) {
                Career tmp = list.get(i);
                sb.append("\nНазвание компании: ").append(tmp.getCompany())
                        .append("\nГода работы: ").append(tmp.getFrom())
                        .append(" - ").append(tmp.getUntil())
                        .append("\nДолжность: ").append(tmp.getPosition());
            }
        }
    }
}


