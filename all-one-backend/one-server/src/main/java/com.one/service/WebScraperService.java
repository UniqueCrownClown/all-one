package com.one.service;

import com.one.dto.OneContentDTO;
import com.one.dto.OneImageDTO;
import com.one.dto.OnePageDTO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebScraperService {

    public Map<String, String> getHtml(String url, String className) {
        Map<String, String> map = new HashMap<>();
        try {
            Document document = Jsoup.connect(url).get();
            Elements select = document.select(className + " p, " + className + " img");
            StringBuilder sb = new StringBuilder();
            for (Element p : select) {
                sb.append(p.outerHtml()).append("\n");
            }
            map.put("content", sb.toString());
            Elements keyword = document.select(".comilla-cerrar");
            map.put("keyword", keyword.text());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OnePageDTO fetchOnePage(String url) {
        try {
            Document document = Jsoup.connect(url).get();


            Elements first = document.select(".carousel-inner .item");
            List<OneImageDTO> carousel = first.stream().map(item -> {
                OneImageDTO oneImageDTO = new OneImageDTO();
                // 获取图片链接
                String imgSrc = item.select(".fp-one-imagen").attr("src");
                // 获取图片描述
                String imgDesc = item.select(".fp-one-imagen-footer").text();
                // 获取标题
                String titulo = item.select(".titulo").text();
                // 获取日期
                String dom = item.select(".dom").text();
                String may = item.select(".may").text();
                // 获取引用文本
                String cita = item.select(".fp-one-cita a").text();
                // 获取引用链接
                String citaHref = item.select(".fp-one-cita a").attr("href");
                oneImageDTO.setOrder(titulo);
                oneImageDTO.setContent(cita);
                oneImageDTO.setImgUrl(imgSrc);
                oneImageDTO.setUrl(citaHref);
                oneImageDTO.setType(imgDesc);
                oneImageDTO.setDateStr(dom + " " + may);
                return oneImageDTO;

            }).collect(Collectors.toList());

            OnePageDTO haha = new OnePageDTO();
            haha.setImages(carousel);

            List<OneContentDTO> oneContentDTOS = new ArrayList<>();
            OneContentDTO oneContentDTO = new OneContentDTO();


            // 文章
            String order = document.select(".fp-one-articulo .one-titulo").text();
            String title = document.select(".fp-one-articulo .one-articulo-titulo").text();
            String author = document.select(".fp-one-articulo .one-articulo-titulo small").text();
            String href = document.select(".fp-one-articulo .one-articulo-titulo a").attr("href");
            Map<String, String> html = getHtml(href, ".articulo-contenido");


            oneContentDTO.setOrder(order + "_" + (short) 1);
            oneContentDTO.setTitle(title);
            oneContentDTO.setUrl(href);
            oneContentDTO.setAuthor(author.substring(author.indexOf("-") + 1));
            oneContentDTO.setType((short) 1);
            oneContentDTO.setIsCurrent((short) 1);
            oneContentDTO.setContent(html.get("content"));
            oneContentDTO.setKeyword(html.get("keyword"));

            oneContentDTOS.add(oneContentDTO);

            oneContentDTO = new OneContentDTO();

            //问题
            order = document.select(".fp-one-cuestion .one-titulo").text();
            title = document.select(".fp-one-cuestion .one-cuestion-titulo").text();
            href = document.select(".fp-one-cuestion .one-cuestion-titulo a").attr("href");

            html = getHtml(href, ".cuestion-contenido");


            oneContentDTO.setOrder(order + "_" + (short) 2);
            oneContentDTO.setTitle(title);
            oneContentDTO.setUrl(href);
            oneContentDTO.setAuthor(null);
            oneContentDTO.setType((short) 2);
            oneContentDTO.setIsCurrent((short) 1);
            oneContentDTOS.add(oneContentDTO);
            oneContentDTO.setContent(html.get("content"));

            String[] typeKind = {".fp-one-articulo", ".fp-one-cuestion"};
            for (String elem : typeKind) {
                Elements select = document.select(elem + " .list-unstyled li");

                List<OneContentDTO> hisOneContentDTOS = select.stream().map(item -> {

                    OneContentDTO ones = new OneContentDTO();
                    Map<String, String> oneHtml;
                    String oneOrder = item.select(".text-muted").text();
                    String oneTitle = item.select("a").text();
                    String oneHref = item.select("a").attr("href");
                    String oneAuthor = item.select("small").text();
                    if (typeKind[0].equals(elem)) {
                        ones.setType((short) 1);
                        oneHtml = getHtml(oneHref, ".articulo-contenido");
                        ones.setKeyword(oneHtml.get("keyword"));
                    } else {
                        ones.setType((short) 2);
                        oneHtml = getHtml(oneHref, ".cuestion-contenido");
                    }
                    ones.setContent(oneHtml.get("content"));
                    ones.setOrder(oneOrder + "_" + ones.getType());
                    ones.setTitle(oneTitle);
                    ones.setUrl(oneHref);
                    ones.setAuthor(oneAuthor.substring(oneAuthor.indexOf("-") + 1));
                    ones.setIsCurrent((short) 1);

                    return ones;
                }).collect(Collectors.toList());
                oneContentDTOS.addAll(hisOneContentDTOS);
            }
            log.info(String.valueOf(oneContentDTOS.size()));

            haha.setContents(oneContentDTOS);
            return haha;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}