class UrlMappings {

	static mappings = {
        "/dante/say"(controller: "quote") {
            action = [GET: "random"]
        }

        "/lead/create"(controller: "lead") {
            action = [POST: "create"]
        }

        "/lead/$id/updateStatus/$status"(controller: "lead") {
            action = [PUT: "updateStatus"]
        }

        "/car/makes"(controller: "car") {
            action = [GET: "makes"]
        }

        "/car/search"(controller: "car") {
            action = [GET: "search"]
        }

        "/car/$id/recommendations"(controller: "car") {
            action = [GET: "recommendations"]
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
