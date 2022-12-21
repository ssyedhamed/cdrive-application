/**
 *
 */
 
 showPrompt=(contactId,currentPage,type)=>{
	const DELETE_URL="/user/"+contactId+"/delete_contact/"+currentPage
	const modal=document.querySelector(".my-prompt-back");
	
	const promptContent=modal.querySelector(".prompt-content")
		console.log(promptContent);
	
	if(type==='delete'){
		
		promptContent.children[0].innerHTML="Do you want to delete this contact?"
		
			promptContent.children[1].classList.add("fa-solid","fa-triangle-exclamation")
		
			
		promptContent.children[2].innerHTML="Once you delete, it cannot be undone. Are you sure?"
		if(modal.children[0].children[1].classList.contains('form')){
			
		modal.children[0].children[1].style.display='none'
		}
		}
			
		

		
	
	if(type==='image'){
		promptContent.children[0].innerHTML="Do you want to update the image?"
		promptContent.children[1].innerHTML=""
		promptContent.children[2].innerHTML=""
		if(modal.children[0].children[1].classList.contains('form')){
			
	modal.children[0].children[2].style.display='none'
		console.log(modal.children[0].children[2])
		}
		
		
	}
	modal.style.display="flex";
	
	modal.children[0].lastElementChild.children[0].addEventListener('click',(e)=>{
		if(contactId!=null){			
			console.log(contactId)
			window.location.href=DELETE_URL
		}else{
			window.location.href="user/view_contacts"+currentPage
		}
	})
	
	
	//alert("Redirect to :"+DELETE_URL)
}
 var myModal = document.getElementById('myModal')
var myInput = document.getElementById('myInput')



const sidebar = document.querySelector(".sidebar");
const toggleBtn = document.querySelector(".toggle-btn");
const content = document.querySelector(".content");
toggleBtn.addEventListener("click", () => {
  if (sidebar.classList.contains("sidebar-hide")) {
    sidebar.classList.remove("sidebar-hide");
    content.classList.remove("move-content");
    toggleBtn.children[0].classList.remove("fa-bars");
    toggleBtn.children[0].classList.add("fa-times");
  } else {
    sidebar.classList.add("sidebar-hide");
    content.classList.add("move-content");
    toggleBtn.children[0].classList.remove("fa-times");
    toggleBtn.children[0].classList.add("fa-bars");
  }
});
