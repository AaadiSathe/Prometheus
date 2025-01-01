const toggleSidebar = () => {
    console.log("enter");
    if ($(".sidebar").is(":visible")) {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

//serach function in serach contact page

const serach =()=>{
  
    let query=$("#search-contact").val();

    if(query=='')
    {
        $(".serach-result").hide();
    }
    else{
       
        //sending request to server
        let url=`http://localhost:8080/serach/${query}`;
        fetch(url).then((response)=>{
            return response.json();
        }).then((data)=>{
           
            
            let text=`<div class='list-group'>`;
            data.forEach((contact)=>{
                text+=`<a href='/user/${contact.cId}/contact' class=list-group-item list-group-action'>${contact.name}</a>`
            })

            text+=`</div>`
            $(".serach-result").html(text);
            $(".serach-result").show();
        })
        
    }
}

const serachs =()=>{
  
   let query=$("#search-contact").val();
   
   if(query=='')    {
       $(".serach-result").hide();
    }
    else{
       
        //sending request to server
        let url=`http://localhost:8080/serach/${query}`;
        fetch(url).then((response)=>{
            return response.json();
        }).then((data)=>{
           
            
           let text=`<div class='list-group'>`;
           data.forEach((contact)=>{
              
               text+=`<a href='/user/show_contact/0?contact_name=${contact.name}' class='list-group-item list-group-action'>${contact.name}</a>`
           })
             text+=`</div>`
            $(".serach-result").html(text);
           $(".serach-result").show();
        })
       
    }
}
const serach_name =()=>{
  
    let query=$("#search-contact-name").val();

 
       
        //sending request to server
        let url="http://localhost:8080/user/serach_name";
    
}

function getState(){
	let ids=document.getElementById("country");
	let state=document.getElementById("state");
	let id=$(ids).val()
	console.log("Countryid: "+id);
	let url=`http://localhost:8080/user/state/${id}`;
        fetch(url).then((response)=>{
           return response.json();
        }).then((data)=>{
           
            
           let text=`<option value='-1'>Select State</option>`;
           data.forEach((state)=>{
              
               text+=`<option value='${state.id}'>${state.name}</option>`
           })
            state.innerHTML=text;
          
        })
       
    }
             
  function getCity(){
	let ids=document.getElementById("state");
	let city=document.getElementById("city");
	let id=$(ids).val()
	console.log("Stateid: "+id);
	let url=`http://localhost:8080/user/city/${id}`;
        fetch(url).then((response)=>{
           return response.json();
        }).then((data)=>{
           
            
           let text=`<option value='-1'>Select City</option>`;
           data.forEach((city)=>{
              
               text+=`<option value='${city.id}'>${city.name}</a>`
           })
            city.innerHTML=text;
          
        })
       
    }          

            
	 function loadFile(event) {
            var image = document.getElementById('profileImage');
            var image1 = document.getElementById('addimg');
            image1.src = URL.createObjectURL(event.target.files[0]);
        }
	
	