import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-token-handler',
  standalone: true,
  imports: [],
  templateUrl: './token-handler.component.html',
  styleUrl: './token-handler.component.css'
})
export class TokenHandlerComponent  implements OnInit {
  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        localStorage.setItem('token', token);
        this.router.navigate(['/home']);
      } else {
        this.router.navigate(['/login'], { queryParams: { error: 'Token not found or invalid' } });
      }
    });
  }
}
