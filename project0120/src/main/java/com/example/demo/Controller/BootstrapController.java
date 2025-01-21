package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BootstrapController {
	
	private ModelAndView mv;
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping("index2")
	public String index2(){
	    log.info("index2");
	    return "bootstrap/index2";
	}
	
	@GetMapping("form-basic")
	public String form_basic(){
	    log.info("aaaaa");
	    return "bootstrap/form-basic";
	}
	
	@GetMapping("advanced-components")
	public String advanced_components(){
	    log.info("advanced-components");
	    return "bootstrap/advanced-components";
	}

	@GetMapping("form-wizard")
	public String form_wizard(){
	    log.info("form-wizard");
	    return "bootstrap/form-wizard";
	}
	
	@GetMapping("html5-editor")
	public String html5_editor(){
	    log.info("html5-editor");
	    return "bootstrap/html5-editor";
	}
	
	@GetMapping("form-pickers")
	public String form_pickers(){
	    log.info("form-pickers");
	    return "bootstrap/form-pickers";
	}
	
	@GetMapping("image-cropper")
	public String image_cropper(){
	    log.info("image-cropper");
	    return "bootstrap/image-cropper";
	}
	
	@GetMapping("image-dropzone")
	public String image_dropzone(){
	    log.info("image-dropzone");
	    return "bootstrap/image-dropzone";
	}
	
	@GetMapping("basic-table")
	public String basic_table(){
	    log.info("basic-table");
	    return "bootstrap/basic-table";
	}
	
	@GetMapping("datatable")
	public String datatable(){
	    log.info("datatable");
	    return "bootstrap/datatable";
	}
	
	@GetMapping("calendar")
	public String calendar(){
	    log.info("calendar");
	    return "bootstrap/calendar";
	}
	
	@GetMapping("ui-buttons")
	public String ui_buttons(){
	    log.info("ui-buttons");
	    return "bootstrap/ui-buttons";
	}
	
	@GetMapping("ui-cards")
	public String ui_cards(){
	    log.info("ui-cards");
	    return "bootstrap/ui-cards";
	}
	
	@GetMapping("ui-cards-hover")
	public String ui_cards_hover(){
	    log.info("ui-cards-hover");
	    return "bootstrap/ui-cards-hover";
	}
	
	@GetMapping("ui-modals")
	public String ui_modals(){
	    log.info("ui-modals");
	    return "bootstrap/ui-modals";
	}
	
	@GetMapping("ui-tabs")
	public String ui_tabs(){
	    log.info("ui-tabs");
	    return "bootstrap/ui-tabs";
	}
	
	@GetMapping("ui-tooltip-popover")
	public String ui_tooltip_popover(){
	    log.info("ui-tooltip-popover");
	    return "bootstrap/ui-tooltip-popover";
	}
	
	@GetMapping("ui-sweet-alert")
	public String ui_sweet_alert(){
	    log.info("ui-sweet-alert");
	    return "bootstrap/ui-sweet-alert";
	}
	
	@GetMapping("ui-notification")
	public String ui_notification(){
	    log.info("ui-notification");
	    return "bootstrap/ui-notification";
	}
	
	@GetMapping("ui-timeline")
	public String ui_timeline(){
	    log.info("ui-timeline");
	    return "bootstrap/ui-timeline";
	}
	
	@GetMapping("ui-progressbar")
	public String ui_progressbar(){
	    log.info("ui-progressbar");
	    return "bootstrap/ui-progressbar";
	}
	
	@GetMapping("ui-typography")
	public String ui_typography(){
	    log.info("ui-typography");
	    return "bootstrap/ui-typography";
	}
	
	@GetMapping("ui-list-group")
	public String ui_list_group(){
	    log.info("ui-list-group");
	    return "bootstrap/ui-list-group";
	}
	
	@GetMapping("ui-range-slider")
	public String ui_range_slider(){
	    log.info("ui-range-slider");
	    return "bootstrap/ui-range-slider";
	}
	
	@GetMapping("ui-carousel")
	public String ui_carousel(){
	    log.info("ui-carousel");
	    return "bootstrap/ui-carousel";
	}
	
	@GetMapping("font-awesome")
	public String font_awesome(){
	    log.info("font-awesome");
	    return "bootstrap/font-awesome";
	}
	
	@GetMapping("foundation")
	public String foundation(){
	    log.info("foundation");
	    return "bootstrap/foundation";
	}
	
	@GetMapping("ionicons")
	public String ionicons(){
	    log.info("ionicons");
	    return "bootstrap/ionicons";
	}
	
	@GetMapping("themify")
	public String themify(){
	    log.info("themify");
	    return "bootstrap/themify";
	}
	
	@GetMapping("custom-icon")
	public String custom_icon(){
	    log.info("custom-icon");
	    return "bootstrap/custom-icon";
	}
	
	@GetMapping("highchart")
	public String highchart(){
	    log.info("highchart");
	    return "bootstrap/highchart";
	}
	
	@GetMapping("knob-chart")
	public String knob_chart(){
	    log.info("knob-chart");
	    return "bootstrap/knob-chart";
	}
	
	@GetMapping("jvectormap")
	public String jvectormap(){
	    log.info("jvectormap");
	    return "bootstrap/jvectormap";
	}
	
	@GetMapping("apexcharts")
	public String apexcharts(){
	    log.info("apexcharts");
	    return "bootstrap/apexcharts";
	}
	
	@GetMapping("video-player")
	public String video_player(){
	    log.info("video-player");
	    return "bootstrap/video-player";
	}
	

	
	@GetMapping("forgot-password")
	public String forgot_password(){
	    log.info("forgot-password");
	    return "bootstrap/forgot-password";
	}
	
	@GetMapping("reset-password")
	public String reset_password(){
	    log.info("reset-password");
	    return "bootstrap/reset-password";
	}
	
	@GetMapping("blank")
	public String blank(){
	    log.info("blank");
	    return "bootstrap/blank";
	}
	
	@GetMapping("contact-directory")
	public String contact_directory(){
	    log.info("contact-directory");
	    return "bootstrap/contact-directory";
	}
	
	@GetMapping("blog")
	public String blog(){
	    log.info("blog");
	    return "bootstrap/blog";
	}
	
	@GetMapping("blog-detail")
	public String blog_detail(){
	    log.info("blog-detail");
	    return "bootstrap/blog-detail";
	}
	
//	@GetMapping("product")
//	public String product(){
//	    log.info("product");
//	    return "bootstrap/product";
//	}
	
	@GetMapping("product-detail")
	public String product_detail(){
	    log.info("product-detail");
	    return "bootstrap/product-detail";
	}
	
	@GetMapping("faq")
	public String faq(){
	    log.info("faq");
	    return "bootstrap/faq";
	}
	
	@GetMapping("profile")
	public String profile(){
	    log.info("profile");
	    return "bootstrap/profile";
	}
	
	@GetMapping("gallery")
	public String gallery(){
	    log.info("gallery");
	    return "bootstrap/gallery";
	}
	
	@GetMapping("pricing-table")
	public String pricing_table(){
	    log.info("pricing-table");
	    return "bootstrap/pricing-table";
	}

	
	

	
}
