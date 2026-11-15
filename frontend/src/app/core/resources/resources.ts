import { inject, Service, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IArrivalMode, IPathology, ITriageColor } from './resources.model';
import { ApiResponse } from '../generics/ApiResponse.model';

@Service()
export class Resources {
  readonly #http = inject(HttpClient);
  readonly #triageColors = signal<ITriageColor[]>([]);
  readonly #pathologies = signal<IPathology[]>([]);
  readonly #arrivalModes = signal<IArrivalMode[]>([]);

  triageColors = this.#triageColors.asReadonly();
  pathologies = this.#pathologies.asReadonly();
  arrivalModes = this.#arrivalModes.asReadonly();

  public fetch() {
    this.fetchPathology();
  }

  private fetchPathology() {
    this.#http
      .get<ApiResponse<IPathology[]>>(`http://localhost:8080/resources/pathologies`)
      .subscribe({
        next: (res) => {
          this.#pathologies.set(res.data.content);
        },
        error: (err) => {
          console.error('Errore durante il fetch delle patologie:', err);
        },
      });
  }
}
