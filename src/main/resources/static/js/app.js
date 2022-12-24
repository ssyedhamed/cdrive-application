/**
 *
 */
 
 showPrompt=(contactId,currentPage,type)=>{
	const DELETE_URL="/user/"+contactId+"/delete_contact/"+currentPage
	const modal=document.querySelector(".my-prompt-back");
	
	const promptContent=modal.querySelector(".prompt-content")
	if(type==='remove-photo'){
		window.location.replace('/user/remove-photo/'+contactId+'/'+currentPage)
		return;
	}
	if(type==='select'){
		const elem= getDialogBox("","Add new photo","Remove photo");
		elem.style.display='flex'
		
		document.body.appendChild(elem)
		
		const buttons=document.body.children[7].children[0].lastElementChild;
		const addButton=buttons.children[0]
		const removeButton=buttons.children[1]
		const cancelButton=buttons.children[2]
		addButton.addEventListener('click',()=>{
			document.body.children[7].style='display:none'
			showPrompt(contactId,currentPage,'image');
		})
		removeButton.addEventListener('click',()=>{
			document.body.children[7].style='display:none'
			showPrompt(contactId,currentPage,'remove-photo');
		})
		cancelButton.addEventListener('click',()=>{
			document.body.children[7].style='display:none'
			window.location.replace('/user/'+contactId+'/show_contact/'+currentPage)
		})
		
		return;
	}
	if(type==='delete'){
		
		promptContent.children[0].innerHTML="Do you want to delete this contact?"
		
			promptContent.children[1].classList.add("fa-solid","fa-triangle-exclamation")
		
			
		promptContent.children[2].innerHTML="Once you delete, it cannot be undone. Are you sure?"
		if(modal.children[0].children[1].classList.contains('form')){
			
		modal.children[0].children[1].style.display='none'
		}
		}
			
		

		
	
	if(type==='image'){
		promptContent.children[0].innerHTML="Change photo"
		promptContent.children[0].style='margin-bottom:10px;'
		
		promptContent.children[1].innerHTML=""
		promptContent.children[2].innerHTML=""
		if(modal.children[0].children[1].classList.contains('form')){
			
	modal.children[0].children[2].style.display='none'
		
		}
		
		
	}
	modal.style.display="flex";
	
	modal.children[0].lastElementChild.children[0].addEventListener('click',(e)=>{
		if(contactId!=null){			
			
			window.location.href=DELETE_URL
		}else{
			window.location.href="user/view_contacts"+currentPage
		}
	})
	
	
	//alert("Redirect to :"+DELETE_URL)
}

const getDialogBox=(content,button1,button2)=>{
	const elem=document.createElement('div')
		elem.innerHTML= `

					<div class="my-prompt text-center  border open-contact-prompt">
						<div class="prompt-content mb-2">
							<h4>${content}</h4>
							
						</div>
						
						<div class="prompt-btns text-center d-flex flex-column ">
							<button class="btn btn-success mb-3" >${button1}</button>
							<button class="btn btn-danger mb-3">${button2}</button>
							<button class="btn btn-outline-success ">Cancel</button>
							
						</div>
					</div>
				`;
		elem.classList.add('my-prompt-back');
						return elem;
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
