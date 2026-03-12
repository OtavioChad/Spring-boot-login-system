package SpringSystemLogin.SpringSystemLogin.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import SpringSystemLogin.SpringSystemLogin.model.Usuario;
import SpringSystemLogin.SpringSystemLogin.model.Ticket;
import SpringSystemLogin.SpringSystemLogin.model.TicketMensagem;
import SpringSystemLogin.SpringSystemLogin.model.TicketStatus;
import SpringSystemLogin.SpringSystemLogin.repository.UsuarioRepository;
import SpringSystemLogin.SpringSystemLogin.repository.TicketMensagemRepository;
import SpringSystemLogin.SpringSystemLogin.repository.TicketRepository;
import SpringSystemLogin.SpringSystemLogin.service.CookieService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private TicketRepository tr;
    
    @Autowired
    private TicketMensagemRepository tmr;
    
    @PostMapping("/tickets/mensagem")
    public String responderTicket(@RequestParam Long ticketId,
                                  @RequestParam String mensagem,
                                  HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if(usuarioId == null){
            return "redirect:/login";
        }

        Ticket ticket = tr.findById(ticketId).orElse(null);

        if(ticket == null){
            return "redirect:/tickets";
        }

        // BLOQUEAR SE FINALIZADO
        if(ticket.getStatus() == TicketStatus.FINALIZADO){
            return "redirect:/tickets";
        }

        TicketMensagem msg = new TicketMensagem();

        msg.setTicketId(ticketId);
        msg.setUsuarioId(Long.parseLong(usuarioId));
        msg.setMensagem(mensagem);
        msg.setData(LocalDateTime.now());

        tmr.save(msg);

        return "redirect:/tickets";
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // LOGIN
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // DASHBOARD
    @GetMapping("/")
    public String dashboard(Model model, HttpServletRequest request) throws Exception {

        model.addAttribute("nome", CookieService.getCookie(request, "nomeUsuario"));
        model.addAttribute("role", CookieService.getCookie(request, "role"));

        return "index";
    }

    // LISTAR TICKETS
    @GetMapping("/tickets")
    public String tickets(Model model, HttpServletRequest request) throws Exception {

        String role = CookieService.getCookie(request, "role");
        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        List<Ticket> tickets;

        if ("ADMIN".equals(role)) {
            tickets = tr.findAll();
        } else {
            tickets = tr.findByUsuarioId(Long.parseLong(usuarioId));
        }

        Map<Long, String> nomesUsuarios = new HashMap<>();
        Map<Long, String> emailsUsuarios = new HashMap<>();

        for (Ticket ticket : tickets) {

            if (!nomesUsuarios.containsKey(ticket.getUsuarioId())) {

                Usuario usuario = ur.findById(ticket.getUsuarioId()).orElse(null);

                if (usuario != null) {
                    nomesUsuarios.put(ticket.getUsuarioId(), usuario.getNome());
                    emailsUsuarios.put(ticket.getUsuarioId(), usuario.getEmail());
                }
            }
        }

        // MAP DO CHAT
        Map<Long, List<TicketMensagem>> ticketMensagens = new HashMap<>();

        for (Ticket ticket : tickets) {

            List<TicketMensagem> mensagens = tmr.findByTicketId(ticket.getId());

            ticketMensagens.put(ticket.getId(), mensagens);

        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("nomesUsuarios", nomesUsuarios);
        model.addAttribute("emailsUsuarios", emailsUsuarios);
        model.addAttribute("ticketMensagens", ticketMensagens);
        model.addAttribute("role", role);

        return "tickets";
    }
    // CRIAR TICKET
    @PostMapping("/tickets/criar")
    public String criarTicket(@RequestParam String titulo,
                              @RequestParam String conteudo,
                              HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if (usuarioId == null) {
            return "redirect:/login";
        }

        Ticket ticket = new Ticket();

        ticket.setTitulo(titulo);
        ticket.setConteudo(conteudo);
        ticket.setStatus(TicketStatus.ABERTO);
        ticket.setUsuarioId(Long.parseLong(usuarioId));
        ticket.setDataCriacao(LocalDateTime.now());

        tr.save(ticket);

        return "redirect:/tickets";
    }
    
    //DELETAR TICKET
    @PostMapping("/tickets/deletar")
    public String deletarTicket(@RequestParam Long id,
                                HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");
        String role = CookieService.getCookie(request, "role");

        if(usuarioId == null){
            return "redirect:/login";
        }

        Ticket ticket = tr.findById(id).orElse(null);

        if(ticket == null){
            return "redirect:/tickets";
        }

        // se for admin pode deletar qualquer ticket
        if("ADMIN".equals(role)){
            tr.delete(ticket);
            return "redirect:/tickets";
        }

        // se for user só pode deletar o próprio
        if(ticket.getUsuarioId().equals(Long.parseLong(usuarioId))){
            tr.delete(ticket);
        }

        return "redirect:/tickets";
    }
    //ALTERAR STATUS
    @PostMapping("/tickets/status")
    public String alterarStatus(@RequestParam Long id,
                                @RequestParam TicketStatus status,
                                HttpServletRequest request) throws Exception {

        String role = CookieService.getCookie(request, "role");

        if(!"ADMIN".equals(role)){
            return "redirect:/tickets";
        }

        Ticket ticket = tr.findById(id).orElse(null);

        if(ticket == null){
            return "redirect:/tickets";
        }

        ticket.setStatus(status);

        tr.save(ticket);

        return "redirect:/tickets";
    }
    
    @GetMapping("/admin/tickets")
    public String adminTickets(Model model, HttpServletRequest request) throws Exception {

        String role = CookieService.getCookie(request, "role");

        if(role == null || !role.equals("ADMIN")){
            return "redirect:/";
        }

        List<Ticket> todosTickets = tr.findAll();

        model.addAttribute("tickets", todosTickets);

        return "adminTickets";
    }
    //Atualizar perfil
    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam String senhaAtual,
                                  HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if(usuarioId == null){
            return "redirect:/login";
        }

        Usuario usuario = ur.findById(Long.parseLong(usuarioId)).orElse(null);

        if(usuario == null){
            return "redirect:/login";
        }

        if(!passwordEncoder.matches(senhaAtual, usuario.getSenha())){
            return "redirect:/perfil?erro=senha";
        }

        // verificar email duplicado
        Usuario existente = ur.findByEmail(email);

        if (existente != null && existente.getId() != usuario.getId()) {
            return "redirect:/perfil?erro=email";
        }

        usuario.setNome(nome);
        usuario.setEmail(email);

        ur.save(usuario);

        return "redirect:/perfil?sucesso=true";
    }

    // PERFIL
    @GetMapping("/perfil")
    public String perfil(Model model, HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if(usuarioId == null){
            return "redirect:/login";
        }

        Usuario usuario = ur.findById(Long.parseLong(usuarioId)).orElse(null);

        if(usuario == null){
            return "redirect:/login";
        }

        List<Ticket> ticketsUsuario = tr.findByUsuarioId(usuario.getId());

        long ticketsCriados = ticketsUsuario.size();

        long ticketsAnalise = ticketsUsuario.stream()
                .filter(t -> t.getStatus() == TicketStatus.EM_ANALISE)
                .count();

        long ticketsFinalizados = ticketsUsuario.stream()
                .filter(t -> t.getStatus() == TicketStatus.FINALIZADO)
                .count();

        model.addAttribute("usuario", usuario);
        model.addAttribute("ticketsCriados", ticketsCriados);
        model.addAttribute("ticketsAnalise", ticketsAnalise);
        model.addAttribute("ticketsFinalizados", ticketsFinalizados);

        return "perfil";
    }

    // CONFIGURAÇÕES
    @GetMapping("/configs")
    public String config(HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        if(usuarioId == null){
            return "redirect:/login";
        }

        return "configs";
    }
    
    @PostMapping("/configs/alterarSenha")
    public String alterarSenha(
            String senhaAtual,
            String novaSenha,
            String confirmarSenha,
            HttpServletRequest request) throws Exception {

        String usuarioId = CookieService.getCookie(request, "usuarioId");

        Usuario usuario = ur.findById(Long.parseLong(usuarioId)).orElse(null);

        if(usuario == null){
            return "redirect:/login";
        }

        if(!passwordEncoder.matches(senhaAtual, usuario.getSenha())){
            return "redirect:/configs?erro=senha";
        }

        if(!novaSenha.equals(confirmarSenha)){
            return "redirect:/configs?erro=confirmacao";
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        ur.save(usuario);

        return "redirect:/login?senhaAlterada";
    }

 // LOGIN POST
    @PostMapping("/logar")
    public String loginUsuario(Usuario usuarioDigitado,
                               Model model,
                               HttpServletResponse response) throws UnsupportedEncodingException {

    	Usuario usuarioBanco = ur.findByEmail(usuarioDigitado.getEmail().trim());

        if (usuarioBanco != null &&
            passwordEncoder.matches(usuarioDigitado.getSenha(), usuarioBanco.getSenha())) {

            CookieService.setCookie(response, "usuarioId",
                    String.valueOf(usuarioBanco.getId()), 10000);

            CookieService.setCookie(response, "nomeUsuario",
                    usuarioBanco.getNome(), 10000);

            CookieService.setCookie(response, "role",
                    usuarioBanco.getRole(), 10000);

            return "redirect:/";
        }

        // LOGIN INVALIDO
        model.addAttribute("erro", "Email ou senha inválidos");

        return "login";
    }

    // SAIR
    @GetMapping("/sair")
    public String sair(HttpServletResponse response) throws UnsupportedEncodingException {

        CookieService.setCookie(response, "usuarioId", "", 0);
        CookieService.setCookie(response, "nomeUsuario", "", 0);
        CookieService.setCookie(response, "role", "", 0);

        return "redirect:/login";
    }

    // CADASTRO
    @GetMapping("/cadastroUsuario")
    public String cadastro(Model model) {

        model.addAttribute("usuario", new Usuario());

        return "cadastro";
    }

    @PostMapping("/cadastroUsuario")
    public String cadastroUsuario(@Valid Usuario usuario, BindingResult result) {

    	if (ur.findByEmail(usuario.getEmail().trim()) != null) {
            result.rejectValue("email", "erro.email", "Email já cadastrado");
        }

        if (!usuario.getSenha().equals(usuario.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", "erro.confirmarSenha", "As senhas não coincidem");
        }

        if (result.hasErrors()) {
            return "cadastro";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setRole("USER");
        ur.save(usuario);

        return "redirect:/login";
    }


    ///ADMIN CONTROLLER
    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model, HttpServletRequest request) throws Exception {

        String role = CookieService.getCookie(request, "role");

        if(role == null || !role.equals("ADMIN")){
            return "redirect:/";
        }

        List<Usuario> usuarios = ur.findAll();

        model.addAttribute("usuarios", usuarios);

        return "adminUsuarios";
    }

    @PostMapping("/admin/usuarios/atualizar")
    public String atualizarUsuario(@RequestParam Long id,
                                   @RequestParam String nome,
                                   @RequestParam String email,
                                   @RequestParam String role,
                                   @RequestParam(required = false) String novaSenha,
                                   @RequestParam String senhaAdmin,
                                   Model model,
                                   HttpServletRequest request) throws Exception {

        String adminId = CookieService.getCookie(request, "usuarioId");
        String roleAdmin = CookieService.getCookie(request, "role");

        if(roleAdmin == null || !roleAdmin.equals("ADMIN")){
            return "redirect:/"; // aqui faz sentido redirecionar mesmo
        }

        Usuario admin = ur.findById(Long.parseLong(adminId)).orElse(null);

        if(admin == null || !passwordEncoder.matches(senhaAdmin, admin.getSenha())){
            model.addAttribute("erro", "senha");
            model.addAttribute("usuarios", ur.findAll());
            return "adminUsuarios";
        }

        Usuario usuario = ur.findById(id).orElse(null);

        if(usuario == null){
            model.addAttribute("erro", "naoencontrado");
            model.addAttribute("usuarios", ur.findAll());
            return "adminUsuarios";
        }

        // Verificar email duplicado
        Usuario usuarioEmail = ur.findByEmail(email);

        if(usuarioEmail != null && usuarioEmail.getId() != id){
            model.addAttribute("erro", "email");
            model.addAttribute("usuarios", ur.findAll());
            return "adminUsuarios";
        }

        // Atualizar usuário
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setRole(role);

        if(novaSenha != null && !novaSenha.isEmpty()){
            usuario.setSenha(passwordEncoder.encode(novaSenha));
        }

        ur.save(usuario);

        model.addAttribute("sucesso", true);
        model.addAttribute("usuarios", ur.findAll());

        return "adminUsuarios";
    }
    @PostMapping("/admin/usuarios/deletar")
    public String deletarUsuario(@RequestParam Long id,
                                 HttpServletRequest request) throws Exception {

        String role = CookieService.getCookie(request, "role");
        String adminId = CookieService.getCookie(request, "usuarioId");

        if(role == null || !role.equals("ADMIN")){
            return "redirect:/";
        }

        if(adminId != null && Long.parseLong(adminId) == id){
            return "redirect:/admin/usuarios?erro=proprioAdmin";
        }

        ur.deleteById(id);

        return "redirect:/admin/usuarios";
    }






}