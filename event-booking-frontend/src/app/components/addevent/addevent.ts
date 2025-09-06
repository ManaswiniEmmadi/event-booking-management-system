import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators,ReactiveFormsModule} from '@angular/forms';
import { finalize } from 'rxjs';
import { EventService } from '../../services/event-service';
import { CreateEvent } from '../../models/eventsdata';

@Component({
  selector: 'app-addevent',
  imports: [ReactiveFormsModule],
  templateUrl: './addevent.html',
  styleUrl: './addevent.css'
})
export class Addevent {
  form: FormGroup;

  isLoading = false;
  successMessage = '';
  errorMessage = '';

  constructor(private formBuilder : FormBuilder,private eventService:EventService){
    this.form = this.formBuilder.group({
    eventName: ['', Validators.required],
    eventDate: ['', Validators.required], // input[type=date] -> 'YYYY-MM-DD'
    location: ['', Validators.required],
    capacity: [1, [Validators.required, Validators.min(1)]],
    description: [''],
    
  }) ;
  }

  onSubmit(){
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const createevent:CreateEvent= this.form.value as CreateEvent;

    this.isLoading = true;
    this.eventService.createEvent(createevent).pipe(
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (created) => {
        this.successMessage = 'Event created successfully';
        this.errorMessage = '';
        
        this.form.reset({ capacity: 1 });
        
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = err?.error?.message || 'Failed to create event';
        this.successMessage = '';
      }
    });
  }


}
