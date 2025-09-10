import { UserService } from "../src/tsx/api/services/UserService";
import { BasicAuthService } from "../src/tsx/api/services/auth/BasicAuthService";
import { UserCreateDto } from "../src/tsx/api/models/dto/UserCreateDto"

const baseUrl = "http://localhost:8080";
const authService = new BasicAuthService("admin", "admin123");
const userService = new UserService(`${baseUrl}/users`, authService);

async function getUserByIdHandler(event: Event) {
  event.preventDefault(); 

  const form = event.target as HTMLFormElement;
  const idInput = form.querySelector<HTMLInputElement>("#id");
  if (!idInput) return;

  const id = Number(idInput.value);

  try {
    const user = await userService.getUserById(id);
    console.log("User fetched successfully:", user);
    alert(JSON.stringify(user, null, 2)); 
  } catch (err) {
    console.error("Error fetching user:", err);
    alert("Error fetching user, see console");
  }
}

async function createUserByIdHandler(event: Event) {
  event.preventDefault();
  const form = event.target as HTMLFormElement;
  const name: string = form.querySelector<HTMLInputElement>("#name")!.value;
  const mail: string = form.querySelector<HTMLInputElement>("#mail")!.value;
  const pass: string = form.querySelector<HTMLInputElement>("#pass")!.value;

  let createdUser: UserCreateDto = {
    username: name,
    email: mail,
    password: pass,
  }
  const user = await userService.createUser(createdUser);
  
}

const getUserByIdForm = document.getElementById("getUserByIdForm") as HTMLFormElement | null;
getUserByIdForm?.addEventListener("submit", getUserByIdHandler);

const createUserForm = document.getElementById("createUserForm") as HTMLFormElement | null;
createUserForm?.addEventListener("submit", createUserByIdHandler);