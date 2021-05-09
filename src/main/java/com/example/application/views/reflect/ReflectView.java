package com.example.application.views.reflect;

import com.example.application.backend.entity.Reflect;
import com.example.application.backend.service.ReflectService;
import com.example.application.backend.service.UserService;
import com.example.application.views.write.WriteView;
import com.github.appreciated.card.RippleClickableCard;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "reflect", layout = MainView.class)
@PageTitle("Reflect")
@CssImport("./views/reflect/reflect-view.css")
public class ReflectView extends VerticalLayout {

    @Autowired
    private WriteView writeView;

    private List<Reflect> posts;
    private ReflectService reflectService;
    private UserService userService;

    public ReflectView(ReflectService reflectService, UserService userService) {
        addClassName("reflect-view");
        this.reflectService = reflectService;
        this.userService = userService;
//        add(new Text("Content placeholder"));
        updateCardList();
    }

    public void updateCardList(){
        posts = reflectService.FindPosts();
        for(Reflect post : posts){
            Div div = new Div();    // For using html
            div.getElement().setProperty("innerHTML", "<h3><strong>Posted by "+userService.findByUserID(post.getUser_id())+"</h3></strong> latest update by "
                                                        +userService.findByUserID(post.getLatest_user_id())+"<hr>"
                                                        +post.getPost()+"<hr>"
                                                        +"<p style='font-size : 10px'><em>"+"posted on "+post.getPostDate()+"</em></p>");
            div.getStyle().set("padding", "20px");
            RippleClickableCard card = new RippleClickableCard(div);
            Component layout = buttonLayout(post.getId());
            layout.setVisible(false);
            card.addClickListener(event -> {
                if(layout.isVisible())
                    layout.setVisible(false);
                else
                    layout.setVisible(true);
            });
            card.add(layout);
            add(card);
        }
    }


    private Component buttonLayout(int post_id) {
        HorizontalLayout layout = new HorizontalLayout();
        Button history = new Button("History");
        Button edit = new Button("Edit");
        edit.addClickListener(event -> {
            List<String> parameter = new ArrayList<>();
            parameter.add(String.valueOf(post_id));
            Map<String, List<String>> queryParameter = new HashMap<>();
            queryParameter.put("id", parameter);
            UI.getCurrent().navigate("write", new QueryParameters(queryParameter));
        });
        layout.add(history, edit);
        return layout;
    }


}
