package logic;

import models.CloudResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CloudManager{

    private Map<String, List<CloudResource>> projects;
    private List<ResourcePolicy<? extends CloudResource>> policies;

    public CloudManager(Map<String, List<CloudResource>> projects, List<ResourcePolicy<? extends CloudResource>> policies) {
        this.projects = projects;
        this.policies = policies;
    }

    /**
     * добавляет ресурс в указанный проект
     * @param projectName Имя проекта, куда добавим ресурс
     * @param resource сам ресурс
     */
    public void addResource(String projectName, CloudResource resource){
        if (!projects.containsKey(projectName)){            //Если среди проектов нет такого, имя которого поступило,
            projects.put(projectName, new ArrayList<>());}  //создать новый лист по этому ключу, и уже по нему вставить
        projects.get(projectName).add(resource);            //новый ресурc
    }

    /**
     * добавляет политику в общий список
     * @param policy Политика: объект класса ResourcePolicy
     */
    public void addPolicy(ResourcePolicy<?> policy){
        policies.add(policy);
    }

    /**
     * роходит по всем проектам, ищет ресурс по заданному ID и возвращает объект
     * @param id объекта
     * @return CloudResource или null
     */
    public CloudResource getResourceById(String id) {
        return getAnalyticsStream()
                .filter(resource -> resource.getId().equals(id))   // Фильтруем по ID
                .findAny()                                                       // Находим любой подходящий
                .orElse(null);                                             // Если не нашли, возвращаем null
    }

    /**
     * проходит по всем ресурсам во всех проектах и
     * применяет к ним каждую политику из списка (внимание: нужно проверять совместимость типов)
     */
    public void applyAllPolicies() {
        for (ResourcePolicy<? extends CloudResource> policy : policies) { // перебор всех политик
            getAnalyticsStream().forEach(resource -> {      // перебор всех ресурсов
                try { //таким образом мы "проверяем" совместимость типов
                    ((ResourcePolicy<CloudResource>) policy).apply(resource);
                } catch (ClassCastException e) {
                    //просто игнорируем
                }
            });
        }
    }


    /**
     * метод, который должен вернуть единый плоский поток данных Stream<CloudResource>,
     * собрав все ресурсы из всех списков словаря projects.
     * Подсказка: используйте projects.values().stream().flatMap(...). Этот поток будет использоваться в
     * классе Main для выполнения аналитики по вариантам
     * @return единый плоский поток данных Stream<CloudResource>
     *     Потоки являються очень удобным методом обработки массивов данных, в методах выше вы можете увидеть почему
     *     глубокое знание методов тоже не требуеться, в принципе в 80% случаев можно сореинтироваться просто
     *     по названию метода
     */
    public Stream<CloudResource> getAnalyticsStream(){
        return projects
                .values()                  //Извлекаем из карты коллекцию всех списков ресурсов
                .stream()                  //Превращаем её в поток списков: Stream<List<CloudResource>>.
                .flatMap(List::stream);    //разворачиваем каждый (flatmap) список в поток (List::stream)
    }

}
